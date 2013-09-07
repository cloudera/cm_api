New Release
===========

- Update `_config.yml` with the new version numbers.
  - Suppose `latest_api_version` is `x`. Then there should be a full set of API docs
    under `apidocs/v{x}`.
  - Suppose `latest_cm_version` is `y`. Then there should be a full set of
    javadocs under `javadocs/{y}`.

- Update `docs/releases.md` to add a new row.


Adding New Content
==================

- Update `_includes/sidenav.html` to link to your new page. Pick the right
  section to put it under. Or feel free to create a new section.
- Create a new Markdown file in `docs/`. The frontmatter in that new file should
  match whatever you put in the sidenav.

Style Guide
-----------
- Ok to use HTML in your Markdown.
- Encouraged to use bootstrap v3 styles.
- Wrap `{% highlight <language> %}` and `{% endhighlight %}` around any code
  block. This will produce a &lt;pre&gt; block with highlighting.
  Do not use the Markdown block indentation with highlighting.
- Use a TOC for long pages. If the last section header is below the fold, it's a
  long page.
- Render locally to preview your page:

      jekyll serve --watch --baseurl=/cm_api

  Also See the GitHub Jekyll
  [guide](https://help.github.com/articles/using-jekyll-with-pages) for more.
