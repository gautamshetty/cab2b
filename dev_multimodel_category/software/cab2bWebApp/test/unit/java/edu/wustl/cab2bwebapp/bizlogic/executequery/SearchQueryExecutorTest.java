package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.server.queryengine.QueryConverter;
import edu.wustl.cab2bwebapp.util.MockQueryObjects;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IRule;

/**
 * @author chetan_patil
 *
 */
public class SearchQueryExecutorTest extends TestCase {

    public void testInsertKeyword() {
        ICab2bQuery query = new MockQueryObjects().createCaFEGeneQuery();
        ICab2bQuery keywordQuery = new QueryConverter().convertToKeywordQuery(query);

        final String keyword = "gmb";
        new SearchQueryExecutor().insertKeyword(keywordQuery, keyword);

        boolean flag = true;
        LOOP: for (IExpression expression : keywordQuery.getConstraints()) {
            for (IExpressionOperand expressionOperand : expression) {
                if (expressionOperand instanceof IRule) {
                    IRule rule = (IRule) expressionOperand;
                    for (ICondition condition : rule) {
                        for (String value : condition.getValues()) {
                            if (!keyword.equals(value)) {
                                flag = false;
                                break LOOP;
                            }
                        }
                    }
                }
            }
        }
        assertTrue(flag);
    }

    public void testTransformResult() throws InterruptedException {
        ICab2bQuery query = new MockQueryObjects().createQuery_GenemRNAProtein();
        Collection<ICab2bQuery> queryList = new ArrayList<ICab2bQuery>(1);
        queryList.add(query);

        SearchQueryExecutor searchQueryExecutor = new SearchQueryExecutor();
        searchQueryExecutor.execute(queryList, null);

        Thread.sleep(5000);
        while (!searchQueryExecutor.isProcessingFinished()) {
            Thread.sleep(5000);
        }
        Thread.sleep(5000);
        Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformedResult =
                searchQueryExecutor.transformResult(100);

        assertFalse(transformedResult.isEmpty());
    }

    /*public void testExecute() {
        ICab2bQuery query = new MockQueryObjects().createQuery_Gene_mRNA_Protein();

        Collection<ICab2bQuery> queries = new ArrayList<ICab2bQuery>();
        queries.add(query);
        SearchQueryExecutor searchQueryExecutor = new SearchQueryExecutor();
        searchQueryExecutor.execute(queries, null);
        Map<ICab2bQuery, TransformedResultObjectWithContactInfo> result = searchQueryExecutor.transformResult(100);
        assertFalse(result.isEmpty());
    }*/
}
