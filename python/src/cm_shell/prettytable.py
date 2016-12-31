#!/usr/bin/env python
#
# Copyright (c) 2009, Luke Maurits <luke@maurits.id.au>
# All rights reserved.
# With contributions from:
#  * Chris Clark
#  * Klein Stephane
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#
# * Redistributions of source code must retain the above copyright notice,
#   this list of conditions and the following disclaimer.
# * Redistributions in binary form must reproduce the above copyright notice,
#   this list of conditions and the following disclaimer in the documentation
#   and/or other materials provided with the distribution.
# * The name of the author may not be used to endorse or promote products
#   derived from this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
# ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
# LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
# POSSIBILITY OF SUCH DAMAGE.

__version__ = "0.6"

import sys
import copy
import random
import textwrap

py3k = sys.version_info[0] >= 3
if py3k:
    unicode = str
    basestring = str
    from html import escape
else:
    from cgi import escape

# hrule styles
FRAME = 0
ALL   = 1
NONE  = 2

# Table styles
DEFAULT = 10
MSWORD_FRIENDLY = 11
PLAIN_COLUMNS = 12
RANDOM = 20

def _get_size(text):
    max_width = 0
    max_height = 0
    text = _unicode(text)
    for line in text.split("\n"):
        max_height += 1
        if len(line) > max_width:
            max_width = len(line)

    return (max_width, max_height)

def _unicode(value, encoding="UTF-8"):
    if not isinstance(value, basestring):
        value = str(value)
    if not isinstance(value, unicode):
        value = unicode(value, encoding, "replace")
    return value

class PrettyTable(object):

    def __init__(self, field_names=None, **kwargs):

        """Return a new PrettyTable instance

        Arguments:

        field_names - list or tuple of field names
        fields - list or tuple of field names to include in displays
        start - index of first data row to include in output
        end - index of last data row to include in output PLUS ONE (list slice style)
        fields - names of fields (columns) to include
        header - print a header showing field names (True or False)
        border - print a border around the table (True or False)
        hrules - controls printing of horizontal rules after rows.  Allowed values: FRAME, ALL, NONE
	int_format - controls formatting of integer data
	float_format - controls formatting of floating point data
        padding_width - number of spaces on either side of column data (only used if left and right paddings are None)
        left_padding_width - number of spaces on left hand side of column data
        right_padding_width - number of spaces on right hand side of column data
        vertical_char - single character string used to draw vertical lines
        horizontal_char - single character string used to draw horizontal lines
        junction_char - single character string used to draw line junctions
        sortby - name of field to sort rows by
        sort_key - sorting key function, applied to data points before sorting
        reversesort - True or False to sort in descending or ascending order"""

        # Data
        self._field_names = []
        self._align = {}
        self._max_width = {}
        self._rows = []
        if field_names:
            self.field_names = field_names
        else:
            self._widths = []
        self._rows = []

        # Options
        self._options = "start end fields header border sortby reversesort sort_key attributes format hrules".split()
        self._options.extend("int_format float_format padding_width left_padding_width right_padding_width".split())
        self._options.extend("vertical_char horizontal_char junction_char".split())
        for option in self._options:
            if option in kwargs:
                self._validate_option(option, kwargs[option])
            else:
                kwargs[option] = None


        self._start = kwargs["start"] or 0
        self._end = kwargs["end"] or None
        self._fields = kwargs["fields"] or None

        self._header = kwargs["header"] or True
        self._border = kwargs["border"] or True
        self._hrules = kwargs["hrules"] or FRAME

        self._sortby = kwargs["sortby"] or None
        self._reversesort = kwargs["reversesort"] or False
        self._sort_key = kwargs["sort_key"] or (lambda x: x)

        self._int_format = kwargs["float_format"] or {}
        self._float_format = kwargs["float_format"] or {}
        self._padding_width = kwargs["padding_width"] or 1
        self._left_padding_width = kwargs["left_padding_width"] or None
        self._right_padding_width = kwargs["right_padding_width"] or None

        self._vertical_char = kwargs["vertical_char"] or "|"
        self._horizontal_char = kwargs["horizontal_char"] or "-"
        self._junction_char = kwargs["junction_char"] or "+"

        self._format = kwargs["format"] or False
        self._attributes = kwargs["attributes"] or {}

    def __getattr__(self, name):

        if name == "rowcount":
            return len(self._rows)
        elif name == "colcount":
            if self._field_names:
                return len(self._field_names)
            elif self._rows:
                return len(self._rows[0])
            else:
                return 0
        else:
            raise AttributeError(name)

    def __getitem__(self, index):

        newtable = copy.deepcopy(self)
        if isinstance(index, slice):
            newtable._rows = self._rows[index]
        elif isinstance(index, int):
            newtable._rows = [self._rows[index],]
        else:
            raise Exception("Index %s is invalid, must be an integer or slice" % str(index))
        return newtable

    def __str__(self):
        if py3k:
            return self.get_string()
        else:
            return self.get_string().encode("ascii","replace")

    def __unicode__(self):
        return self.get_string()

    ##############################
    # ATTRIBUTE VALIDATORS       #
    ##############################

    # The method _validate_option is all that should be used elsewhere in the code base to validate options.
    # It will call the appropriate validation method for that option.  The individual validation methods should
    # never need to be called directly (although nothing bad will happen if they *are*).
    # Validation happens in TWO places.
    # Firstly, in the property setters defined in the ATTRIBUTE MANAGMENT section.
    # Secondly, in the _get_options method, where keyword arguments are mixed with persistent settings

    def _validate_option(self, option, val):
        if option in ("start", "end", "padding_width", "left_padding_width", "right_padding_width", "format"):
            self._validate_nonnegative_int(option, val)
        elif option in ("sortby"):
            self._validate_field_name(option, val)
        elif option in ("sort_key"):
            self._validate_function(option, val)
        elif option in ("hrules"):
            self._validate_hrules(option, val)
        elif option in ("fields"):
            self._validate_all_field_names(option, val)
        elif option in ("header", "border", "reversesort"):
            self._validate_true_or_false(option, val)
        elif option in ("int_format"):
            self._validate_int_format(option, val)
        elif option in ("float_format"):
            self._validate_float_format(option, val)
        elif option in ("vertical_char", "horizontal_char", "junction_char"):
            self._validate_single_char(option, val)
        elif option in ("attributes"):
            self._validate_attributes(option, val)
        else:
            raise Exception("Unrecognised option: %s!" % option)

    def _validate_align(self, val):
        try:
            assert val in ["l","c","r"]
        except AssertionError:
            raise Exception("Alignment %s is invalid, use l, c or r!" % val)

    def _validate_nonnegative_int(self, name, val):
        try:
            assert int(val) >= 0
        except AssertionError:
            raise Exception("Invalid value for %s: %s!" % (name, _unicode(val)))

    def _validate_true_or_false(self, name, val):
        try:
            assert val in (True, False)
        except AssertionError:
            raise Exception("Invalid value for %s!  Must be True or False." % name)

    def _validate_int_format(self, name, val):
        if val == "":
            return
        try:
            assert type(val) in (str, unicode)
            assert val.isdigit()
        except AssertionError:
            raise Exception("Invalid value for %s!  Must be an integer format string." % name)

    def _validate_float_format(self, name, val):
        if val == "":
            return
        try:
            assert type(val) in (str, unicode)
            assert "." in val
            bits = val.split(".")
            assert len(bits) <= 2
            assert bits[0] == "" or bits[0].isdigit()
            assert bits[1] == "" or bits[1].isdigit()
        except AssertionError:
            raise Exception("Invalid value for %s!  Must be a float format string." % name)

    def _validate_function(self, name, val):
        try:
            assert hasattr(val, "__call__")
        except AssertionError:
            raise Exception("Invalid value for %s!  Must be a function." % name)

    def _validate_hrules(self, name, val):
        try:
            assert val in (ALL, FRAME, NONE)
        except AssertionError:
            raise Exception("Invalid value for %s!  Must be ALL, FRAME or NONE." % name)

    def _validate_field_name(self, name, val):
        try:
            assert val in self._field_names
        except AssertionError:
            raise Exception("Invalid field name: %s!" % val)

    def _validate_all_field_names(self, name, val):
        try:
            for x in val:
                self._validate_field_name(name, x)
        except AssertionError:
            raise Exception("fields must be a sequence of field names!")

    def _validate_single_char(self, name, val):
        try:
            assert len(_unicode(val)) == 1
        except AssertionError:
            raise Exception("Invalid value for %s!  Must be a string of length 1." % name)

    def _validate_attributes(self, name, val):
        try:
            assert isinstance(val, dict)
        except AssertionError:
            raise Exception("attributes must be a dictionary of name/value pairs!")

    ##############################
    # ATTRIBUTE MANAGEMENT       #
    ##############################

    def _get_field_names(self):
        return self._field_names
        """The names of the fields

        Arguments:

        fields - list or tuple of field names"""
    def _set_field_names(self, val):
        if self._field_names:
            old_names = self._field_names[:]
        self._field_names = val
        if self._align and old_names:
            for old_name, new_name in zip(old_names, val):
                self._align[new_name] = self._align[old_name]
            for old_name in old_names:
                self._align.pop(old_name)
        else:
            for field in self._field_names:
                self._align[field] = "c"
    field_names = property(_get_field_names, _set_field_names)

    def _get_align(self):
        return self._align
    def _set_align(self, val):
        self._validate_align(val)
        for field in self._field_names:
            self._align[field] = val
    align = property(_get_align, _set_align)

    def _get_max_width(self):
        return self._max_width
    def _set_max_width(self, val):
        self._validate_nonnegativeint(val)
        for field in self._field_names:
            self._max_width[field] = val
    max_width = property(_get_max_width, _set_max_width)

    def _get_start(self):
        """Start index of the range of rows to print

        Arguments:

        start - index of first data row to include in output"""
        return self._start

    def _set_start(self, val):
        self._validate_option("start", val)
        self._start = val
    start = property(_get_start, _set_start)

    def _get_end(self):
        """End index of the range of rows to print

        Arguments:

        end - index of last data row to include in output PLUS ONE (list slice style)"""
        return self._end
    def _set_end(self, val):
        self._validate_option("end", val)
        self._end = val
    end = property(_get_end, _set_end)

    def _get_sortby(self):
        """Name of field by which to sort rows

        Arguments:

        sortby - field name to sort by"""
        return self._sortby
    def _set_sortby(self, val):
        self._validate_option("sortby", val)
        self._sortby = val
    sortby = property(_get_sortby, _set_sortby)

    def _get_reversesort(self):
        """Controls direction of sorting (ascending vs descending)

        Arguments:

        reveresort - set to True to sort by descending order, or False to sort by ascending order"""
        return self._reversesort
    def _set_reversesort(self, val):
        self._validate_option("reversesort", val)
        self._reversesort = val
    reversesort = property(_get_reversesort, _set_reversesort)

    def _get_sort_key(self):
        """Sorting key function, applied to data points before sorting

        Arguments:

        sort_key - a function which takes one argument and returns something to be sorted"""
        return self._sort_key
    def _set_sort_key(self, val):
        self._validate_option("sort_key", val)
        self._sort_key = val
    sort_key = property(_get_sort_key, _set_sort_key)

    def _get_header(self):
        """Controls printing of table header with field names

        Arguments:

        header - print a header showing field names (True or False)"""
        return self._header
    def _set_header(self, val):
        self._validate_option("header", val)
        self._header = val
    header = property(_get_header, _set_header)

    def _get_border(self):
        """Controls printing of border around table

        Arguments:

        border - print a border around the table (True or False)"""
        return self._border
    def _set_border(self, val):
        self._validate_option("border", val)
        self._border = val
    border = property(_get_border, _set_border)

    def _get_hrules(self):
        """Controls printing of horizontal rules after rows

        Arguments:

        hrules - horizontal rules style.  Allowed values: FRAME, ALL, NONE"""
        return self._hrules
    def _set_hrules(self, val):
        self._validate_option("hrules", val)
        self._hrules = val
    hrules = property(_get_hrules, _set_hrules)

    def _get_int_format(self):
        """Controls formatting of integer data
        Arguments:

        int_format - integer format string"""
        return self._int_format
    def _set_int_format(self, val):
        self._validate_option("int_format", val)
        for field in self._field_names:
            self._int_format[field] = val
    int_format = property(_get_int_format, _set_int_format)

    def _get_float_format(self):
        """Controls formatting of floating point data
        Arguments:

        float_format - floating point format string"""
        return self._float_format
    def _set_float_format(self, val):
        self._validate_option("float_format", val)
        for field in self._field_names:
            self._float_format[field] = val
    float_format = property(_get_float_format, _set_float_format)

    def _get_padding_width(self):
        """The number of empty spaces between a column's edge and its content

        Arguments:

        padding_width - number of spaces, must be a positive integer"""
        return self._padding_width
    def _set_padding_width(self, val):
        self._validate_option("padding_width", val)
        self._padding_width = val
    padding_width = property(_get_padding_width, _set_padding_width)

    def _get_left_padding_width(self):
        """The number of empty spaces between a column's left edge and its content

        Arguments:

        left_padding - number of spaces, must be a positive integer"""
        return self._left_padding_width
    def _set_left_padding_width(self, val):
        self._validate_option("left_padding_width", val)
        self._left_padding_width = val
    left_padding_width = property(_get_left_padding_width, _set_left_padding_width)

    def _get_right_padding_width(self):
        """The number of empty spaces between a column's right edge and its content

        Arguments:

        right_padding - number of spaces, must be a positive integer"""
        return self._right_padding_width
    def _set_right_padding_width(self, val):
        self._validate_option("right_padding_width", val)
        self._right_padding_width = val
    right_padding_width = property(_get_right_padding_width, _set_right_padding_width)

    def _get_vertical_char(self):
        """The charcter used when printing table borders to draw vertical lines

        Arguments:

        vertical_char - single character string used to draw vertical lines"""
        return self._vertical_char
    def _set_vertical_char(self, val):
        self._validate_option("vertical_char", val)
        self._vertical_char = val
    vertical_char = property(_get_vertical_char, _set_vertical_char)

    def _get_horizontal_char(self):
        """The charcter used when printing table borders to draw horizontal lines

        Arguments:

        horizontal_char - single character string used to draw horizontal lines"""
        return self._horizontal_char
    def _set_horizontal_char(self, val):
        self._validate_option("horizontal_char", val)
        self._horizontal_char = val
    horizontal_char = property(_get_horizontal_char, _set_horizontal_char)

    def _get_junction_char(self):
        """The charcter used when printing table borders to draw line junctions

        Arguments:

        junction_char - single character string used to draw line junctions"""
        return self._junction_char
    def _set_junction_char(self, val):
        self._validate_option("vertical_char", val)
        self._junction_char = val
    junction_char = property(_get_junction_char, _set_junction_char)

    def _get_format(self):
        """Controls whether or not HTML tables are formatted to match styling options

        Arguments:

        format - True or False"""
        return self._format
    def _set_format(self, val):
        self._validate_option("format", val)
        self._format = val
    format = property(_get_format, _set_format)

    def _get_attributes(self):
        """A dictionary of HTML attribute name/value pairs to be included in the <table> tag when printing HTML

        Arguments:

        attributes - dictionary of attributes"""
        return self._attributes
    def _set_attributes(self, val):
        self.validate_option("attributes", val)
        self._attributes = val
    attributes = property(_get_attributes, _set_attributes)

    ##############################
    # OPTION MIXER               #
    ##############################

    def _get_options(self, kwargs):

        options = {}
        for option in self._options:
            if option in kwargs:
                self._validate_option(option, kwargs[option])
                options[option] = kwargs[option]
            else:
                options[option] = getattr(self, "_"+option)
        return options

    ##############################
    # PRESET STYLE LOGIC         #
    ##############################

    def set_style(self, style):

        if style == DEFAULT:
            self._set_default_style()
        elif style == MSWORD_FRIENDLY:
            self._set_msword_style()
        elif style == PLAIN_COLUMNS:
            self._set_columns_style()
        elif style == RANDOM:
            self._set_random_style()
        else:
            raise Exception("Invalid pre-set style!")

    def _set_default_style(self):

        self.header = True
        self.border = True
        self._hrules = FRAME
        self.padding_width = 1
        self.left_padding_width = 1
        self.right_padding_width = 1
        self.vertical_char = "|"
        self.horizontal_char = "-"
        self.junction_char = "+"

    def _set_msword_style(self):

        self.header = True
        self.border = True
        self._hrules = NONE
        self.padding_width = 1
        self.left_padding_width = 1
        self.right_padding_width = 1
        self.vertical_char = "|"

    def _set_columns_style(self):

        self.header = True
        self.border = False
        self.padding_width = 1
        self.left_padding_width = 0
        self.right_padding_width = 8

    def _set_random_style(self):

        # Just for fun!
        self.header = random.choice((True, False))
        self.border = random.choice((True, False))
        self._hrules = random.choice((ALL, FRAME, NONE))
        self.left_padding_width = random.randint(0,5)
        self.right_padding_width = random.randint(0,5)
        self.vertical_char = random.choice("~!@#$%^&*()_+|-=\{}[];':\",./;<>?")
        self.horizontal_char = random.choice("~!@#$%^&*()_+|-=\{}[];':\",./;<>?")
        self.junction_char = random.choice("~!@#$%^&*()_+|-=\{}[];':\",./;<>?")

    ##############################
    # DATA INPUT METHODS         #
    ##############################

    def add_row(self, row):

        """Add a row to the table

        Arguments:

        row - row of data, should be a list with as many elements as the table
        has fields"""

        if self._field_names and len(row) != len(self._field_names):
            raise Exception("Row has incorrect number of values, (actual) %d!=%d (expected)" %(len(row),len(self._field_names)))
        self._rows.append(list(row))

    def del_row(self, row_index):

        """Delete a row to the table

        Arguments:

        row_index - The index of the row you want to delete.  Indexing starts at 0."""

        if row_index > len(self._rows)-1:
            raise Exception("Can't delete row at index %d, table only has %d rows!" % (row_index, len(self._rows)))
        del self._rows[row_index]

    def add_column(self, fieldname, column, align="c"):

        """Add a column to the table.

        Arguments:

        fieldname - name of the field to contain the new column of data
        column - column of data, should be a list with as many elements as the
        table has rows
        align - desired alignment for this column - "l" for left, "c" for centre and "r" for right"""

        if len(self._rows) in (0, len(column)):
            self._validate_align(align)
            self._field_names.append(fieldname)
            self._align[fieldname] = align
            for i in range(0, len(column)):
                if len(self._rows) < i+1:
                    self._rows.append([])
                self._rows[i].append(column[i])
        else:
            raise Exception("Column length %d does not match number of rows %d!" % (len(column), len(self._rows)))

    def clear_rows(self):

        """Delete all rows from the table but keep the current field names"""

        self._rows = []

    def clear(self):

        """Delete all rows and field names from the table, maintaining nothing but styling options"""

        self._rows = []
        self._field_names = []
        self._widths = []

    ##############################
    # MISC PUBLIC METHODS        #
    ##############################

    def copy(self):
        return copy.deepcopy(self)

    ##############################
    # MISC PRIVATE METHODS       #
    ##############################

    def _format_value(self, field, value):
        if isinstance(value, int) and field in self._int_format:
            value = ("%%%sd" % self._int_format[field]) % value
        elif isinstance(value, float) and field in self._float_format:
            value = ("%%%sf" % self._float_format[field]) % value
        return value

    def _compute_widths(self, rows, options):
        if options["header"]:
            widths = [_get_size(field)[0] for field in self._field_names]
        else:
            widths = len(self.field_names) * [0]
        for row in rows:
            for index, value in enumerate(row):
                value = self._format_value(self.field_names[index], value)
                widths[index] = max(widths[index], _get_size(_unicode(value))[0])
        self._widths = widths

    def _get_padding_widths(self, options):

        if options["left_padding_width"] is not None:
            lpad = options["left_padding_width"]
        else:
            lpad = options["padding_width"]
        if options["right_padding_width"] is not None:
            rpad = options["right_padding_width"]
        else:
            rpad = options["padding_width"]
        return lpad, rpad

    def _get_rows(self, options):
        """Return only those data rows that should be printed, based on slicing and sorting.

        Arguments:

        options - dictionary of option settings."""

	# Make a copy of only those rows in the slice range
        rows = copy.deepcopy(self._rows[options["start"]:options["end"]])
        # Sort if necessary
        if options["sortby"]:
            sortindex = self._field_names.index(options["sortby"])
            # Decorate
            rows = [[row[sortindex]]+row for row in rows]
            # Sort
            rows.sort(reverse=options["reversesort"], key=options["sort_key"])
            # Undecorate
            rows = [row[1:] for row in rows]
        return rows

    ##############################
    # PLAIN TEXT STRING METHODS  #
    ##############################

    def get_string(self, **kwargs):

        """Return string representation of table in current state.

        Arguments:

        start - index of first data row to include in output
        end - index of last data row to include in output PLUS ONE (list slice style)
        fields - names of fields (columns) to include
        header - print a header showing field names (True or False)
        border - print a border around the table (True or False)
        hrules - controls printing of horizontal rules after rows.  Allowed values: FRAME, ALL, NONE
	int_format - controls formatting of integer data
	float_format - controls formatting of floating point data
        padding_width - number of spaces on either side of column data (only used if left and right paddings are None)
        left_padding_width - number of spaces on left hand side of column data
        right_padding_width - number of spaces on right hand side of column data
        vertical_char - single character string used to draw vertical lines
        horizontal_char - single character string used to draw horizontal lines
        junction_char - single character string used to draw line junctions
        sortby - name of field to sort rows by
        sort_key - sorting key function, applied to data points before sorting
        reversesort - True or False to sort in descending or ascending order"""

        options = self._get_options(kwargs)

        bits = []

        # Don't think too hard about an empty table
        if self.rowcount == 0:
            return ""

        rows = self._get_rows(options)
        self._compute_widths(rows, options)

        # Build rows
        # (for now, this is done before building headers etc. because rowbits.append
        # contains width-adjusting voodoo which has to be done first.  This is ugly
        # and Wrong and will change soon)
        rowbits = []
        for row in rows:
            rowbits.append(self._stringify_row(row, options))


        # Add header or top of border
        if options["header"]:
            bits.append(self._stringify_header(options))
        elif options["border"] and options["hrules"] != NONE:
            bits.append(self._hrule)

        # Add rows
        bits.extend(rowbits)

        # Add bottom of border
        if options["border"] and not options["hrules"]:
            bits.append(self._hrule)

        string = "\n".join(bits)
        self._nonunicode = string
        return _unicode(string)

    def _stringify_hrule(self, options):

        if not options["border"]:
            return ""
        lpad, rpad = self._get_padding_widths(options)
        bits = [options["junction_char"]]
        for field, width in zip(self._field_names, self._widths):
            if options["fields"] and field not in options["fields"]:
                continue
            bits.append((width+lpad+rpad)*options["horizontal_char"])
            bits.append(options["junction_char"])
        return "".join(bits)

    def _stringify_header(self, options):

        bits = []
        lpad, rpad = self._get_padding_widths(options)
        if options["border"]:
            if options["hrules"] != NONE:
                bits.append(self._hrule)
                bits.append("\n")
            bits.append(options["vertical_char"])
        for field, width, in zip(self._field_names, self._widths):
            if options["fields"] and field not in options["fields"]:
                continue
            if self._align[field] == "l":
                bits.append(" " * lpad + _unicode(field).ljust(width) + " " * rpad)
            elif self._align[field] == "r":
                bits.append(" " * lpad + _unicode(field).rjust(width) + " " * rpad)
            else:
                bits.append(" " * lpad + _unicode(field).center(width) + " " * rpad)
            if options["border"]:
                bits.append(options["vertical_char"])
        if options["border"] and options["hrules"] != NONE:
            bits.append("\n")
            bits.append(self._hrule)
        return "".join(bits)

    def _stringify_row(self, row, options):

        for index, value in enumerate(row):
            row[index] = self._format_value(self.field_names[index], value)

        for index, field, value, width, in zip(range(0,len(row)), self._field_names, row, self._widths):
            # Enforce max widths
            max_width = self._max_width.get(field, 0)
            lines = _unicode(value).split("\n")
            new_lines = []
            for line in lines:
                if max_width and len(line) > max_width:
                    line = textwrap.fill(line, max_width)
                new_lines.append(line)
            lines = new_lines
            value = "\n".join(lines)
            row[index] = value

        #old_widths = self._widths[:]

        for index, field in enumerate(self._field_names):
            namewidth = len(field)
            datawidth = min(self._widths[index], self._max_width.get(field, self._widths[index]))
            if options["header"]:
               self._widths[index] = max(namewidth, datawidth)
            else:
               self._widths[index] = datawidth

        row_height = 0
        for c in row:
            h = _get_size(c)[1]
            if h > row_height:
                row_height = h

        bits = []
        lpad, rpad = self._get_padding_widths(options)
        for y in range(0, row_height):
            bits.append([])
            if options["border"]:
                bits[y].append(self.vertical_char)

        for field, value, width, in zip(self._field_names, row, self._widths):

            lines = _unicode(value).split("\n")
            if len(lines) < row_height:
                lines = lines + ([""] * (row_height-len(lines)))

            y = 0
            for l in lines:
                if options["fields"] and field not in options["fields"]:
                    continue

                if self._align[field] == "l":
                    bits[y].append(" " * lpad + _unicode(l).ljust(width) + " " * rpad)
                elif self._align[field] == "r":
                    bits[y].append(" " * lpad + _unicode(l).rjust(width) + " " * rpad)
                else:
                    bits[y].append(" " * lpad + _unicode(l).center(width) + " " * rpad)
                if options["border"]:
                    bits[y].append(self.vertical_char)

                y += 1

        self._hrule = self._stringify_hrule(options)

        if options["border"] and options["hrules"]== ALL:
            bits[row_height-1].append("\n")
            bits[row_height-1].append(self._hrule)

        for y in range(0, row_height):
            bits[y] = "".join(bits[y])

        #self._widths = old_widths

        return "\n".join(bits)

    ##############################
    # HTML STRING METHODS        #
    ##############################

    def get_html_string(self, **kwargs):

        """Return string representation of HTML formatted version of table in current state.

        Arguments:

        start - index of first data row to include in output
        end - index of last data row to include in output PLUS ONE (list slice style)
        fields - names of fields (columns) to include
        header - print a header showing field names (True or False)
        border - print a border around the table (True or False)
        hrules - controls printing of horizontal rules after rows.  Allowed values: FRAME, ALL, NONE
	int_format - controls formatting of integer data
	float_format - controls formatting of floating point data
        padding_width - number of spaces on either side of column data (only used if left and right paddings are None)
        left_padding_width - number of spaces on left hand side of column data
        right_padding_width - number of spaces on right hand side of column data
        sortby - name of field to sort rows by
        sort_key - sorting key function, applied to data points before sorting
        attributes - dictionary of name/value pairs to include as HTML attributes in the <table> tag"""

        options = self._get_options(kwargs)

        if options["format"]:
            string = self._get_formatted_html_string(options)
        else:
            string = self._get_simple_html_string(options)

        self._nonunicode = string
        return _unicode(string)

    def _get_simple_html_string(self, options):

        bits = []
        # Slow but works
        table_tag = '<table'
        if options["border"]:
            table_tag += ' border="1"'
        if options["attributes"]:
            for attr_name in options["attributes"]:
                table_tag += ' %s="%s"' % (attr_name, options["attributes"][attr_name])
        table_tag += '>'
        bits.append(table_tag)

        # Headers
        if options["header"]:
            bits.append("    <tr>")
            for field in self._field_names:
                if options["fields"] and field not in options["fields"]:
                    continue
                bits.append("        <th>%s</th>" % escape(_unicode(field)).replace("\n", "<br />"))
            bits.append("    </tr>")

        # Data
        rows = self._get_rows(options)
        for row in rows:
            bits.append("    <tr>")
            for field, datum in zip(self._field_names, row):
                if options["fields"] and field not in options["fields"]:
                    continue
                bits.append("        <td>%s</td>" % escape(_unicode(datum)).replace("\n", "<br />"))
            bits.append("    </tr>")

        bits.append("</table>")
        string = "\n".join(bits)

        self._nonunicode = string
        return _unicode(string)

    def _get_formatted_html_string(self, options):

        bits = []
        lpad, rpad = self._get_padding_widths(options)
        # Slow but works
        table_tag = '<table'
        if options["border"]:
            table_tag += ' border="1"'
        if options["hrules"] == NONE:
            table_tag += ' frame="vsides" rules="cols"'
        if options["attributes"]:
            for attr_name in options["attributes"]:
                table_tag += ' %s="%s"' % (attr_name, options["attributes"][attr_name])
        table_tag += '>'
        bits.append(table_tag)
        # Headers
        if options["header"]:
            bits.append("    <tr>")
            for field in self._field_names:
                if options["fields"] and field not in options["fields"]:
                    continue
                bits.append("        <th style=\"padding-left: %dem; padding-right: %dem; text-align: center\">%s</th>" % (lpad, rpad, escape(_unicode(field)).replace("\n", "<br />")))
            bits.append("    </tr>")
        # Data
        rows = self._get_rows(options)
        for row in self._rows:
            bits.append("    <tr>")
            for field, datum in zip(self._field_names, row):
                if options["fields"] and field not in options["fields"]:
                    continue
                if self._align[field] == "l":
                    bits.append("        <td style=\"padding-left: %dem; padding-right: %dem; text-align: left\">%s</td>" % (lpad, rpad, escape(_unicode(datum)).replace("\n", "<br />")))
                elif self._align[field] == "r":
                    bits.append("        <td style=\"padding-left: %dem; padding-right: %dem; text-align: right\">%s</td>" % (lpad, rpad, escape(_unicode(datum)).replace("\n", "<br />")))
                else:
                    bits.append("        <td style=\"padding-left: %dem; padding-right: %dem; text-align: center\">%s</td>" % (lpad, rpad, escape(_unicode(datum)).replace("\n", "<br />")))
            bits.append("    </tr>")
        bits.append("</table>")
        string = "\n".join(bits)

        self._nonunicode = string
        return _unicode(string)

def main():

    x = PrettyTable(["City name", "Area", "Population", "Annual Rainfall"])
    x.sortby = "Population"
    x.reversesort = True
    x.int_format["Area"] = "04"
    x.float_format = "6.1"
    x.align["City name"] = "l" # Left align city names
    x.add_row(["Adelaide", 1295, 1158259, 600.5])
    x.add_row(["Brisbane", 5905, 1857594, 1146.4])
    x.add_row(["Darwin", 112, 120900, 1714.7])
    x.add_row(["Hobart", 1357, 205556, 619.5])
    x.add_row(["Sydney", 2058, 4336374, 1214.8])
    x.add_row(["Melbourne", 1566, 3806092, 646.9])
    x.add_row(["Perth", 5386, 1554769, 869.4])
    print(x)

if __name__ == "__main__":
    main()
