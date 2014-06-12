## WARNING

This is a very early development status. Also it works as a proof of concept, there are non 
functioning buttons, and it does not find all papers, even if they are listed on the sites.

## What it is:

Autocomplete is a plugin for JabRef, which trys to autocomplete the entrys in your database using information provided by ACM, SpringerLink, IEEE, DBLP, CiteSeer and others.
At the moment these sites are implementet:
- ACM
- SpringerLink
- IEEE

## Install

Download the [JabRefAutocomplete.jar](https://github.com/gumulka/JabRefAutocomplete/blob/master/JabRefAutocomplete.jar?raw=true) Go to Plugins -> Manage Plugins. Click Install plugin, and choose the jar file. Now restart JabRef and you are done.

## Use

Select one or more BibTex entrys where you wish to have more information and click the Autocomplete button.

at the moment it works best, if you have a doi spezified, otherwise it needs a title.
If Authors are filled out, the results can get better.
This may change in future versions.

## Links

To use this plugin, you need: 
- [JabRef](http://jabref.sourceforge.net/)

Autocomplete uses this great library:
- [JSoup](http://jsoup.org/)


## Contribute
I only know about the websites, where papers are published I read. So if there are some sites, you think should be included in this tool, feel free to [mail](mailto:jabrefautocomplete@gummu.de) me with a link to an example paper on this site.

Planned:
- CiteSeer
- DBLP
- ScienceDirect
- arXiv.org
- oxfordjournals.org
