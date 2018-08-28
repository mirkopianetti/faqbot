package dao;

import it.trentinodigitale.faq.utils.ApplicationBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CommonParams;

import java.util.List;

public class FaqSolr {

    protected ApplicationBuilder applicationBuilder = new ApplicationBuilder();

    public List<SolrDocument> getResult(String azienda,
                                        String categoria,
                                        String sottocategoria,
                                        String testo,
                                        Integer numeroRisultatiMax) throws Exception{

        SolrQuery query = new SolrQuery();
        if (categoria!=null) {
            categoria = "categoria:\"" + categoria + "\"";
            query.set(CommonParams.FQ,categoria);
        }

        if (azienda!=null) {
            azienda = "azienda:\"" + azienda + "\"";
            query.set(CommonParams.FQ,azienda);
        }

        if (sottocategoria!=null) {
            sottocategoria = "sottocategoria:\"" + sottocategoria + "\"";
            query.set(CommonParams.FQ,sottocategoria);
        }

        if (numeroRisultatiMax!=null){
            query.setRows(numeroRisultatiMax);
        }

        query.set(CommonParams.Q,testo);


        query.setRequestHandler(applicationBuilder.getMessageApplication("solr.requestHandler"));
        String url = applicationBuilder.getMessageApplication("solr.url");



        HttpSolrClient.Builder s = new HttpSolrClient.Builder(url);
        SolrClient solrClient = s.build();

        QueryResponse queryResponse = solrClient.query(query);
        return queryResponse.getResults();


    }

}
