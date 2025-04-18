/*
 * Copyright © 2010 Talis Systems Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openjena.earq.indexers;

import org.openjena.earq.EARQException;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.larq.LARQ;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;

public class ModelIndexerSubject extends ModelIndexerBase {
    Property property ;
    
    public ModelIndexerSubject(String url) { 
    	super(url) ; 
    }

    public ModelIndexerSubject(Property p, String url) {
        this(url) ;
        property = p ;
    }
    
    @Override
    public void unindexStatement(Statement s) { 
        if ( ! indexThisStatement(s) )
            return ;

        try {
            Node subject = s.getSubject().asNode() ;

            if ( ! s.getObject().isLiteral() || ! LARQ.isString(s.getLiteral()) )
                return ;

            Node object  = s.getObject().asNode() ;
            
            builder.unindex(subject, object.getLiteralLexicalForm()) ;
        } catch (Exception e)
        { throw new EARQException("unindexStatement", e) ; }
    }
    
    @Override
    public void indexStatement(Statement s) {
        if ( ! indexThisStatement(s) ) {
            return ;
        }
        
        try {
            Node subject = s.getSubject().asNode() ;

            if ( ! s.getObject().isLiteral() || ! LARQ.isString(s.getLiteral()) ) {
                return ;
            }
            
            Node object  = s.getObject().asNode() ;
            
            // TODO: double check this!
            // Note: if a subject occurs twice with an indexable string, there will be two hits later.
            builder.index(subject, object.getLiteralLexicalForm()) ;
        } catch (Exception e) { 
        	throw new EARQException("indexStatement", e) ; 
        }
    }

    protected boolean indexThisStatement(Statement s) {  
        if ( property == null ) { 
            return true ;
        }
        return s.getPredicate().equals(property) ;
    }
}