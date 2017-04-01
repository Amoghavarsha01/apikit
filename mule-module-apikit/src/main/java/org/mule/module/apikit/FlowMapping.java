/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.apikit;

import org.mule.runtime.core.api.construct.Flow;

public class FlowMapping
{

    private String resource;
    private String action;
    private String contentType;
    private String flowRef;
    private Flow flow;

    public String getResource()
    {
        return resource;
    }

    public void setResource(String resource)
    {
        this.resource = resource;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getKey() {
        String key = action + ":" + resource;
        if (contentType != null)
        {
            key += ":" + contentType;
        }
        return key;
    }

    public String getFlowRef() {
        return flowRef;
    }

    public void setFlowRef(String flowRef){
        this.flowRef = flowRef;
    }

    public void setFlow(Flow flow){
        this.flow = flow;
    }

    public Flow getFlow(){
        return flow;
    }
}
