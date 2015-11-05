MIaSMath is a math processing plugin for Lucene or Solr.

To integrate MIaSMath including MathTokenizer into a Solr instance:

1. Following libraries must be copied to solr/lib directory:
	-jdom2-2.0.3.jar
	-mathml-canonicalizer.jar
	-MIaSMath.jar

2. Configuration in the schema.xml must be done:
  
The following attributes must be specified for the tokenizer MathTokenizer:
  subformulae - true for analyzer type index, false for analyzer type query
 
Complete example:
<fieldType name="math" class="solr.TextField">
  <analyzer type="index">
    <tokenizer class="cz.muni.fi.mias.MathTokenizerFactory" subformulae="true"/> 
  </analyzer>
  <analyzer type="query">
    <tokenizer class="cz.muni.fi.mias.MathTokenizerFactory" subformulae="false"/> 
  </analyzer>
</fieldType>


A field for storing math must be declared:
<field name="math" type="math" indexed="true" stored="false" multiValued="true" />

That's it. You can run your Solr instance and test MathTokenizer in the analysis interface.

