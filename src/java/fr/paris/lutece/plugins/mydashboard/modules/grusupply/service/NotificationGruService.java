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
package fr.paris.lutece.plugins.mydashboard.modules.grusupply.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import fr.paris.lutece.plugins.grustoragedb.business.DemandType;
import fr.paris.lutece.plugins.grusupply.web.rs.DemandResult;
import fr.paris.lutece.plugins.grusupply.web.rs.NotificationResult;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.util.MydashboardGrusupplyUtil;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

/**
 * 
 * NotificationGruService
 *
 */
public class NotificationGruService
{
    // BEAN
    public static final String  BEAN_NAME                           = "mydashboard-grusupply.notificationGruService";

    // PROPERTIES
    private static final String PROPERTY_HOST                       = AppPropertiesService.getProperty( "mydashboard-grusupply.api.rest.demand_notification.host" );
    private static final String PROPERTY_DEMAND_LIST_ENDPOINT       = AppPropertiesService.getProperty( "mydashboard-grusupply.api.rest.demand_list.endpoint" );
    private static final String PROPERTY_NOTIFICATION_LIST_ENDPOINT = AppPropertiesService.getProperty( "mydashboard-grusupply.api.rest.notification_list.endpoint" );
    private static final String PROPERTY_DEMANDTYPE_LIST_ENDPOINT   = AppPropertiesService.getProperty( "mydashboard-grusupply.api.rest.demand_type_list.endpoint" );

    // PARAMETERS
    private static final String PARAMETER_CUSTOMER_ID               = "customerId";
    private static final String PARAMETER_INDEX                     = "index";
    private static final String PARAMETER_READED                    = "readed";
    private static final String PARAMETER_ID_DEMAND                 = "idDemand";
    private static final String PARAMETER_ID_DEMAND_TYPE            = "idDemandType";

    /**
     * Constructor
     */
    private NotificationGruService( )
    {

    }

    /**
     * Gets list of demand
     * 
     * @param strCustomerId
     * @param strIndex
     * @return list of demand
     */
    public DemandResult getListDemand( String strCustomerId, String strIndex )
    {
        DemandResult demandResult = new DemandResult( );

        Map<String, String> headers = new HashMap<>( );
        headers.put( PARAMETER_CUSTOMER_ID, strCustomerId );
        headers.put( PARAMETER_INDEX, strIndex );

        try
        {
            HttpAccess httpAccess = new HttpAccess( );
            String result = httpAccess.doGet( PROPERTY_HOST + PROPERTY_DEMAND_LIST_ENDPOINT, null, null, headers );

            demandResult = MydashboardGrusupplyUtil.getObjectMapper( ).readValue( result, DemandResult.class );

        } catch ( HttpAccessException | JsonProcessingException e )
        {
            AppLogService.error( "Error while reading JSON of list demand for customer id {} ", strCustomerId, e );
        }

        return demandResult;
    }


    /**
     * Gets list of notification
     * @param strIdDemand
     * @param strIdDemandType
     * @param strIsRead
     * @return list of notification
     */
    public NotificationResult getListNotification( String strIdDemand, String strIdDemandType, String strIsRead )
    {
        NotificationResult notificationResult = new NotificationResult( );

        Map<String, String> headers = new HashMap<>( );
        headers.put( PARAMETER_ID_DEMAND, strIdDemand );
        headers.put( PARAMETER_ID_DEMAND_TYPE, strIdDemandType );
        headers.put( PARAMETER_READED, strIsRead );

        try
        {
            HttpAccess httpAccess = new HttpAccess( );
            String result = httpAccess.doGet( PROPERTY_HOST + PROPERTY_NOTIFICATION_LIST_ENDPOINT, null, null, headers );

            notificationResult = MydashboardGrusupplyUtil.getObjectMapper( ).readValue( result, NotificationResult.class );

        } catch ( HttpAccessException | JsonProcessingException e )
        {
            AppLogService.error( "Error while reading JSON for notification id demand {} ", strIdDemand, e );
        }

        return notificationResult;
    }

    /**
     * Get list of demand types
     * 
     * @return list of demand type
     */
    public List<DemandType> getListDemandType( )
    {
        List<DemandType> listDemandType = new ArrayList<>( );
        
        try
        {
            HttpAccess httpAccess = new HttpAccess( );
            String result = httpAccess.doGet( PROPERTY_HOST + PROPERTY_DEMANDTYPE_LIST_ENDPOINT );

            listDemandType = MydashboardGrusupplyUtil.getObjectMapper( ).readValue( result, new TypeReference<List<DemandType>>( )
            {
            } );

        } catch ( HttpAccessException | JsonProcessingException e )
        {
            AppLogService.error( "Error while reading JSON for demand type", e );
        }

        return listDemandType;
    }
}
