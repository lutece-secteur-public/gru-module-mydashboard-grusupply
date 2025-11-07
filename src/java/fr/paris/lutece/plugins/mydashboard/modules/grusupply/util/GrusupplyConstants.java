/*
 * Copyright (c) 2002-2023, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.mydashboard.modules.grusupply.util;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * GrusupplyConstants
 *
 */
public class GrusupplyConstants
{
    /**
     * Private construtor
     */
    private GrusupplyConstants( )
    {
        // Do nothing
    }

    // MARKS
    public static final String MARK_DEMAND_TYPE_LIST              = "demand_types_list";
    public static final String MARK_LIST_DEMAND                   = "list_demands";
    public static final String MARK_NB_ITEMS_PER_PAGE             = "nb_items_per_page";
    public static final String MARK_PAGINATOR                     = "paginator";
    public static final String MARK_LIST_CATEGORIES               = "categoryList";

    // PROPERTIES
    public static final String PROPERTY_LIMIT_NOTIFICATION_RESULT = AppPropertiesService.getProperty( "mydashboard-grusupply.limit.result.lastnotification", "5" );
    public static final String PROPERTY_TAG_RDV                   = AppPropertiesService.getProperty( "mydashboard-grusupply.tag.rdv", "rdv" );
    public static final String PROPERTY_TAG_FACTURE               = AppPropertiesService.getProperty( "mydashboard-grusupply.tag.facture", "facture" );
    public static final String PROPERTY_LIST_STATUS_BILLING       = "mydashboard-grusupply.list.status.billing";
    public static final String PROPERTY_NUMBER_OF_DEMAND_PER_PAGE = "mydashboard-grusupply.limit.result.notification";
    public static final String PROPERTY_URL_MES_DEMARCHES         = "mydashboard-grusupply.url.mesdemarches";
    
    // PARAMETERS
    public static final String PARAMETER_CATEGORY_CODE            = "cat";
    public static final String PARAMETER_PANEL                    = "panel";
    public static final String PARAMETER_INPUT_DATE               = "date";
    public static final String PARAMETER_INPUT_SEARCH             = "search";
    public static final String SESSION_CATEGORIES                 = "categories";
    public static final String SESSION_CUSTOMER_ID                = "customerId";

}
