/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     
 */
package org.nuxeo.labs;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.publisher.api.PublicationTree;
import org.nuxeo.ecm.platform.publisher.api.PublisherService;
import org.nuxeo.ecm.platform.rendition.publisher.RenditionPublicationFactory;
import org.nuxeo.template.adapters.doc.TemplateBindings;

/**
 * @author fvadon
 */
@Operation(id = PublishTemplateRenditionOperation.ID, category = Constants.CAT_DOCUMENT, label = "Publish Template Rendition", description = "Operation to publish a rendition of a document, the parameters are the target section and the template name: delivery for example. "
        + "Gives back the original document for now (should give the published proxy of the document in the next version).")
public class PublishTemplateRenditionOperation {

    public static final String ID = "PublishTemplateRendition";

    @Context
    protected CoreSession session;

    @Context
    protected PublisherService service;

    @Param(name = "templateName")
    protected String templateName = TemplateBindings.DEFAULT_BINDING;

    @Param(name = "target")
    protected DocumentModel target;

    @OperationMethod(collector = DocumentModelCollector.class)
    public DocumentModel run(DocumentModel doc) throws ClientException {
        Map<String, String> params = new HashMap<String, String>();
        PublicationTree tree = service.getPublicationTreeFor(target, session);
        params.put(RenditionPublicationFactory.RENDITION_NAME_PARAMETER_KEY,
                templateName);
        service.publish(doc, tree.getNodeByPath(target.getPathAsString()),
                params);

        return doc;
    }

}
