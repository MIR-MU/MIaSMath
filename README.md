MIaSMath – Math processing for [Lucene][] / [Solr][]
====================================================
[![CircleCI](https://circleci.com/gh/MIR-MU/MIaSMath/tree/master.svg?style=shield)][ci]

 [ci]: https://circleci.com/gh/MIR-MU/MIaSMath/tree/master (CircleCI)

[MIaSMath][mias] is a math processing plugin for [Lucene][] or [Solr][].

 [lucene]: https://lucene.apache.org/
 [mathmlcan]: https://github.com/MIR-MU/MathMLCan
 [mias]: https://mir.fi.muni.cz/mias/
 [solr]: https://lucene.apache.org/solr/

Usage
=====
To integrate MIaSMath including MathTokenizer into a Solr instance:

1. Copy the following libraries to the `solr/lib` directory:

  - `jdom2-2.0.3.jar`
	- [`mathml-canonicalizer.jar`][mathmlcan]
	- `MIaSMath.jar`

2. Configure the following attributes in `schema.xml` for the tokenizer
   MathTokenizer:

  - `subformulae` – `true` for analyzer type `index`, and `false` for analyzer
    type `query`, as follows:
    
    ``` xml
    <fieldType name="math" class="solr.TextField">
      <analyzer type="index">
        <tokenizer class="cz.muni.fi.mias.MathTokenizerFactory" subformulae="true"/> 
      </analyzer>
      <analyzer type="query">
        <tokenizer class="cz.muni.fi.mias.MathTokenizerFactory" subformulae="false"/> 
      </analyzer>
    </fieldType>
    ```

  - Declare a field for storing math as follows:
    
    ``` xml
    <field name="math" type="math" indexed="true" stored="false" multiValued="true" />
    ```

That's it. You can now run your Solr instance and test MathTokenizer in the
analysis interface.

Citing MIaSMath
===============
Text
----
SOJKA, Petr and Martin LÍŠKA. The Art of Mathematics Retrieval. In Matthew R.
B. Hardy, Frank Wm. Tompa. *Proceedings of the 2011 ACM Symposium on Document
Engineering.* Mountain View, CA, USA: ACM, 2011. p. 57–60. ISBN
978-1-4503-0863-2. doi:[10.1145/2034691.2034703][doi].

 [doi]: http://doi.org/10.1145/2034691.2034703

BibTeX
------
``` bib
@inproceedings{doi:10.1145:2034691.2034703,
     author = "Petr Sojka and Martin L{\'\i}{\v s}ka",
      title = "{The Art of Mathematics Retrieval}",
  booktitle = "{Proceedings of the ACM Conference on Document Engineering,
  		DocEng 2011}",
  publisher = "{Association of Computing Machinery}",
    address = "{Mountain View, CA}",
       year = 2011,
      month = Sep,
       isbn = "978-1-4503-0863-2",
      pages = "57--60",
        url = {http://doi.acm.org/10.1145/2034691.2034703},
        doi = {10.1145/2034691.2034703},
   abstract = {The design and architecture of MIaS (Math Indexer and Searcher), 
	       a system for mathematics retrieval is presented, and design 
	       decisions are discussed. We argue for an approach based on 
	       Presentation MathML using a similarity of math subformulae. The 
	       system was implemented as a math-aware search engine based on the 
	       state-of-the-art system Apache Lucene. Scalability issues were 
	       checked against more than 400,000 arXiv documents with 158 
	       million mathematical formulae. Almost three billion MathML 
	       subformulae were indexed using a Solr-compatible Lucene.},
}
```
