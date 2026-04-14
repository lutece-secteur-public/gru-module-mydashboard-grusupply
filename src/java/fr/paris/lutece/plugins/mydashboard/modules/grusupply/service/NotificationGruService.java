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

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.demand.DemandCategory;
import fr.paris.lutece.plugins.grubusiness.business.demand.DemandType;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.DemandResult;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.NotificationResult;
import fr.paris.lutece.plugins.grubusiness.service.notification.NotificationException;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.DemandDashboard;
import fr.paris.lutece.plugins.notificationstore.v1.web.service.NotificationStoreService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * NotificationGruService
 *
 */
public class NotificationGruService
{
    private static NotificationGruService _notificationGruService;
    private static NotificationStoreService _notificationStoreService;
    private static final String    DESC                            = "DESC";

    /**
     * Constructor
     */
    private NotificationGruService( )
    {        
    }
    
    /**
     * 
     * @return instance of NotificationGruService
     */
    public static NotificationGruService getInstance (  )
    {
        if(  _notificationGruService == null )
        {
            _notificationGruService = new NotificationGruService( );
            
            List<NotificationStoreService> listNotificationStoreService = SpringContextService.getBeansOfType( NotificationStoreService.class );
            
            if( CollectionUtils.isNotEmpty( listNotificationStoreService ) )
            {
                _notificationStoreService =  listNotificationStoreService.get( 0 );
            }
        }
        
        return _notificationGruService;
    }

    
    /**
     * Gets list of demand
     * 
     * @param strCustomerId
     * @param strIndex
     * @param strLimitResult
     * @param strNotificationType
     * @return list of demand
     */
    public DemandResult getListDemand( String strCustomerId, String strIndex, String strLimitResult, String strNotificationType )
    {
        try
        {
            return _notificationStoreService.getListDemand( strCustomerId, null, strIndex, strLimitResult, strNotificationType, DESC );
        }
        catch ( NotificationException e )
        {
            AppLogService.error( "Une erreur s'est produite lors de la récupération de la liste des demandes de l'utilisateur {}",strCustomerId, e.getMessage( ) );
        }
        
        return null;
    }

    /**
     * Gets list of demand by list of status
     * 
     * @param strCustomerId
     * @param strListStatus
     * @param strIdsDemandType
     * @param strIndex
     * @param strLimitResult
     * @param strNotificationType
     * @param strCategoryCode
     * @return list of demand by list of status
     */
    public DemandResult getListDemandByStatus( String strCustomerId, String strListStatus, String strIdsDemandType, String strIndex, String strLimitResult, String strNotificationType, String strCategoryCode )
    {
        try
        {
            return _notificationStoreService.getListOfDemandByStatus( strCustomerId, strListStatus, strIdsDemandType, strIndex, strLimitResult, strNotificationType, strCategoryCode );
        }
        catch ( NotificationException e )
        {
            AppLogService.error( "Une erreur s'est produite lors de la récupération de la liste des demandes par statut de l'utilisateur {}",strCustomerId, e.getMessage( ) );
        }
        
        return null;
    }
    

    /**
     * Get list of notification
     * @param strCustomerId
     * @param listDemandPairs
     *            list of maps with keys "demandId" and "demandTypeId"
     * @param strNotificationType
     * @return list of notification
     */
    public NotificationResult getListNotification( String strCustomerId, List<Map<String, String>> listDemandPairs, String strNotificationType)
    {        
        try
        {   
            return _notificationStoreService.getNotificationsByDemandList(strCustomerId, listDemandPairs, strNotificationType);
        } catch ( NotificationException e )
        {
            AppLogService.error( "Une erreur s'est produite lors de la récupération de la liste des notifications de l'utilisateur {}", strCustomerId, e.getMessage( ) );
        }
        return null;
    }

    /**
     * Get list of demand types
     * 
     * @return list of demand type
     */
    public List<DemandType> getListDemandType( )
    {
        try
        {
            return _notificationStoreService.getDemandTypes( );
            
        } catch ( Exception e )
        {
            AppLogService.error( "Une erreur s'est produite lors de la récupération de la liste des types de demande", e.getMessage( ) );  
        }
        return Collections.emptyList();
    }
    
    /**
     * Get list of demand categories
     * @return list of demand categories
     */
    public List<DemandCategory> getListDemandCategories( )
    {
        try
        {
            return _notificationStoreService.getCategoriesList( );
            
        } catch ( Exception e )
        {
            AppLogService.error( "Une erreur s'est produite lors de la récupération de la liste des catégories", e.getMessage( ) );  
        }
        return Collections.emptyList(); 
    }
    
    /**
     * Filter demand by date
     * 
     * @return list of demand dashboard filtered by date
     */
    public List<DemandDashboard> filterByDate( List<DemandDashboard> listDemandDashboards, LocalDate inputLocalDate )
    {
        List<DemandDashboard> filteredList = new ArrayList<>( );

        for ( DemandDashboard dashboard : listDemandDashboards )
        {
            Demand demand = dashboard.getDemand( );
            List<Notification> notifications = dashboard.getListNotification( );

            boolean matchesDate = false;

            if ( demand != null )
            {
                LocalDate creationDate = Instant.ofEpochMilli( demand.getCreationDate( ) ).atZone( ZoneId.systemDefault( ) ).toLocalDate( );
                LocalDate modifyDate = Instant.ofEpochMilli( demand.getModifyDate( ) ).atZone( ZoneId.systemDefault( ) ).toLocalDate( );
                if ( creationDate.equals( inputLocalDate ) || modifyDate.equals( inputLocalDate ) )
                {
                    matchesDate = true;
                }
            }
            
            if ( !matchesDate && notifications != null )
            {
                for ( Notification notif : notifications )
                {
                    if ( notif.getDate( ) != null )
                    {
                        LocalDate notifDate = Instant.ofEpochMilli( notif.getDate( ) ).atZone( ZoneId.systemDefault( ) ).toLocalDate( );
                        if ( notifDate.equals( inputLocalDate ) )
                        {
                            matchesDate = true;
                            break;
                        }
                    }
                }
            }
            
            if ( matchesDate )
            {
                filteredList.add( dashboard );
            }
        }

        return filteredList;
    }
    
    /**
     * Filter demand by keyword
     * 
     * @return list of demand dashboard filtered by keyword
     */
    public List<DemandDashboard> filterByKeyword( List<DemandDashboard> listDemandDashboards, String inputKeyword, List<DemandType> listDemandType )
    {
        List<DemandDashboard> filteredList = new ArrayList<>( );

        for ( DemandDashboard dashboard : listDemandDashboards )
        {
            List<Notification> notifications = dashboard.getListNotification( );

            boolean matchesKeyword = false;

            if ( notifications != null )
            {
                for ( Notification notif : notifications )
                {
                    if ( notif.getMyDashboardNotification( ) != null )
                    {
                        if ( notif.getMyDashboardNotification( ).getMessage( ) != null && notif.getMyDashboardNotification( ).getMessage( ).toLowerCase( ).contains( inputKeyword ) )
                        {
                            matchesKeyword = true;
                            break;
                        }

                        if ( notif.getMyDashboardNotification( ).getStatusText( ) != null && notif.getMyDashboardNotification( ).getStatusText( ).toLowerCase( ).contains( inputKeyword ) )
                        {
                            matchesKeyword = true;
                            break;
                        }

                        if ( notif.getMyDashboardNotification( ).getSubject( ) != null && notif.getMyDashboardNotification( ).getSubject( ).toLowerCase( ).contains( inputKeyword ) )
                        {
                            matchesKeyword = true;
                            break;
                        }

                        if ( notif.getMyDashboardNotification( ).getSenderName( ) != null && notif.getMyDashboardNotification( ).getSenderName( ).toLowerCase( ).contains( inputKeyword ) )
                        {
                            matchesKeyword = true;
                            break;
                        }
                    }
                }
            }
            
            Demand demand = dashboard.getDemand( );
            if ( demand != null && demand.getTypeId( ) != null && listDemandType != null )
            {
                for ( DemandType demandType : listDemandType )
                {
                    if ( String.valueOf( demandType.getIdDemandType( ) ).equals( demand.getTypeId( ) ) && 
                         demandType.getLabel( ) != null && demandType.getLabel( ).toLowerCase( ).contains( inputKeyword ) )
                    {
                        matchesKeyword = true;
                        break;
                    }
                }
            }

            if ( matchesKeyword )
            {
                filteredList.add( dashboard );
            }
        }

        return filteredList;
    }
    
}