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
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.paris.lutece.plugins.grubusiness.business.web.rs.DemandResult;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.NotificationResult;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.DemandType;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.util.MydashboardGrusupplyUtil;
import fr.paris.lutece.plugins.notificationstore.v1.web.rs.service.NotificationStoreTransportRest;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * 
 * NotificationGruService
 *
 */
public class NotificationGruService
{
    // BEAN
    public static final String  BEAN_NAME                           = "mydashboard-grusupply.notificationGruService";

    @Inject
    @Named("mydashboard-grusupply.notificationstore.restservice")
    private NotificationStoreTransportRest _notificationStoreProvider;
    
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
     * @param strNotificationType
     * @return list of demand
     */
    public DemandResult getListDemand( String strCustomerId, String strIndex, String strNotificationType )
    {
        return _notificationStoreProvider.getListDemand( strCustomerId, null, strIndex, strNotificationType );
    }


    /**
     * Gets list of notification
     * @param strIdDemand
     * @param strIdDemandType
     * @param strIsRead
     * @return list of notification
     */
    public NotificationResult getListNotification( String strIdDemand, String strIdDemandType, String strCustomerId )
    {        
        return _notificationStoreProvider.getListNotification( strCustomerId, strIdDemand, strIdDemandType );        
    }

    /**
     * Get list of demand types
     * 
     * @return list of demand type
     */
    public List<DemandType> getListDemandType( )
    {
        String jsonDemandType = _notificationStoreProvider.getDemandTypes( );
        
        List<DemandType> listDemandType = new ArrayList< >( ) ;
        try
        {
            listDemandType = MydashboardGrusupplyUtil.getObjectMapper( ).readValue( jsonDemandType, new TypeReference<List<DemandType>>( ){} );
        }
        catch( final Exception e )
        {
            AppLogService.error( "LibraryNotificationstore - Error HttpAccessTransport :" + e.getMessage( ), e );
        }
        return listDemandType;
    }
}
