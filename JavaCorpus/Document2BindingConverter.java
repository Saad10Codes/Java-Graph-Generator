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

package org.openjena.earq;

import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.binding.BindingMap;
import com.hp.hpl.jena.sparql.util.NodeFactory;
import com.hp.hpl.jena.util.iterator.Map1;

class Document2BindingConverter implements Map1<Document,Binding> {

	private Binding binding ;
    private Var subject ;
    private Var score ;
    
    Document2BindingConverter(Binding binding, Var subject, Var score) {
        this.binding = binding ;
        this.subject = subject ;
        this.score = score ;
    }
    
    public Binding map1(Document doc) {
        Binding b = new BindingMap(binding) ;
        b.add(Var.alloc(subject), EARQ.build(doc)) ;
        if ( score != null ) {
            b.add(Var.alloc(score), NodeFactory.floatToNode(Float.parseFloat(doc.get(EARQ.fScore)))) ;
        }
        return b ;
    }
    
}