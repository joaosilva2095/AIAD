package aiad.feup.agents;

import aiad.feup.models.Company;
import aiad.feup.exceptions.MalformedObjectException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Investor Unit Tests
 */
public class InvestorUnitTest {

    /**
     * List with all companies
     */
    private List<Company> companies;

    @Before
    public void setup() throws MalformedObjectException {
        this.companies = new ArrayList<>();
        this.companies.add(new Company("Coca-Cola", 100, false, 50));
    }

    @Test
    public void investmentAddition() throws Exception {
        assertEquals(4, 2 + 2);
    }
}
