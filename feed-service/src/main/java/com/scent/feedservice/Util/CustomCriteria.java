package com.scent.feedservice.Util;

import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CustomCriteria extends Criteria {


    private List<Object> criteriaChain;
    private LinkedHashMap<String, Object> criteria = new LinkedHashMap<>();

    public CustomCriteria() {
        this.criteriaChain = new ArrayList<>();
    }
    /**
     * Creates a geospatial criterion using a {@literal $geoWithin $centerSphere} operation. This is only available for
     * Mongo 2.4 and higher.
     *
     * @param circle must not be {@literal null}
     * @return
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/geoWithin/">MongoDB Query operator:
     *      $geoWithin</a>
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/centerSphere/">MongoDB Query operator:
     *      $centerSphere</a>
     */
    @Override
    public Criteria withinSphere(Circle circle) {

        Assert.notNull(circle, "Circle must not be null!");
        Query q = new Query(new Criteria().withinSphere(circle));
        criteria.put("location", q);
        return this;
    }
}
