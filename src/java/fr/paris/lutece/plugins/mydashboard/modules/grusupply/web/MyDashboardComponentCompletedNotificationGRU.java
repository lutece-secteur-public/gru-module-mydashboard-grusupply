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
package fr.paris.lutece.plugins.mydashboard.modules.grusupply.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.grubusiness.business.demand.DemandStatus;
import fr.paris.lutece.plugins.grubusiness.business.notification.EnumNotificationType;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.DemandDisplay;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.DemandResult;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.EnumGenericStatus;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.NotificationResult;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.DemandDashboard;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.DemandDashboardHome;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.service.IdentityStoreService;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.service.NotificationGruService;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.util.LocalizedDelegatePaginator;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * MyDashboardComponentCompletedNotificationGRU
 *
 */
public class MyDashboardComponentCompletedNotificationGRU extends MyDashboardComponent
{

    /**
     * 
     */
    private static final long      serialVersionUID                   = 8297192924908575568L;

    // CONSTANTS
    private static final String    TEMPLATE_NOTIFICATION_LIST         = "skin/plugins/mydashboard/modules/grusupply/dashboard_completed_demand.html";
    private static final String    DASHBOARD_COMPONENT_ID             = "mydashboard-grusupply.componentCompletedNotif";
    private static final String    MESSAGE_COMPONENT_DESCRIPTION      = "module.mydashboard.grusupply.myDashboardComponentCompletedNotification.description";
    private static final String    CURRENT_PAGE_INDEX                 = "current_page_index";

    // PROPERTIES
    private static final String    PROPERTY_NUMBER_OF_DEMAND_PER_PAGE = "mydashboard-grusupply.limit.result.notification";
    private static final String    PROPERTY_URL_MES_DEMARCHES         = "mydashboard-grusupply.url.mesdemarches";

    // MARKS
    private static final String    MARK_LIST_DEMAND                   = "list_demands";
    private static final String    MARK_NB_ITEMS_PER_PAGE             = "nb_items_per_page";
    private static final String    MARK_PAGINATOR                     = "paginator";
    private static final String    MARK_DEMAND_TYPE_LIST              = "demand_types_list";
    
    // PARAMETERS
    private static final String    PARAMETER_CATEGORY_CODE            = "cat";

    @Inject
    @Named( NotificationGruService.BEAN_NAME )
    private NotificationGruService _notificationService;

    @Override
    public String getDashboardData( HttpServletRequest request )
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        String categoryCode = request.getParameter( PARAMETER_CATEGORY_CODE );
        
        if ( user != null )
        {
            Map<String, Object> model = new HashMap<>( );

            HttpSession session = request.getSession( true );

            String strCurrentPageIndex = session.getAttribute( CURRENT_PAGE_INDEX ) != null ? ( String ) session.getAttribute( CURRENT_PAGE_INDEX ) : null;

            strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, strCurrentPageIndex );
            session.setAttribute( CURRENT_PAGE_INDEX, strCurrentPageIndex );

            int nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_NUMBER_OF_DEMAND_PER_PAGE, 10 );

            IdentityDto identity = IdentityStoreService.getIdentityByGuid( user.getName( ) );
            DemandResult demandResult = _notificationService.getListDemandByStatus( identity.getCustomerId( ), getListStatusCompleted( ) ,strCurrentPageIndex, String.valueOf( nDefaultItemsPerPage ), EnumNotificationType.MYDASHBOARD.toString( ), categoryCode );

            // PAGINATOR
            if( demandResult != null && CollectionUtils.isNotEmpty( demandResult.getListDemandDisplay( ) ) )
            {
                LocalizedDelegatePaginator<DemandDisplay> paginator = new LocalizedDelegatePaginator<>( demandResult.getListDemandDisplay( ), nDefaultItemsPerPage,
                        AppPropertiesService.getProperty( PROPERTY_URL_MES_DEMARCHES ), AbstractPaginator.PARAMETER_PAGE_INDEX, strCurrentPageIndex, demandResult.getNumberResult( ),
                        request.getLocale( ) );
    
                List<DemandDashboard> listDemandDashboards = getDemandDashboardList( identity.getCustomerId( ), paginator );                
                setModel( model, nDefaultItemsPerPage, paginator, listDemandDashboards );
            }

            HtmlTemplate htmTemplate = AppTemplateService.getTemplate( TEMPLATE_NOTIFICATION_LIST, request.getLocale( ), model );

            return htmTemplate.getHtml( );
        }

        return StringUtils.EMPTY;
    }

    @Override
    public String getComponentId( )
    {
        return DASHBOARD_COMPONENT_ID;
    }

    @Override
    public String getComponentDescription( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_COMPONENT_DESCRIPTION, locale );
    }
    
    /**
     * Get demand dashboard list
     * @param user
     * @param paginator
     * @return list of demand dashboard
     */
    private List<DemandDashboard> getDemandDashboardList( String strCustomerId, LocalizedDelegatePaginator<DemandDisplay> paginator )
    {
        List<DemandDashboard> listDemandDashboards = new ArrayList<>( );
        
        if ( CollectionUtils.isNotEmpty( paginator.getPageItems( ) ) )
        {
            for( DemandDisplay demand : paginator.getPageItems( ) )
            {
                NotificationResult notificationList = _notificationService.getListNotification( demand.getDemand( ).getId( ), demand.getDemand( ).getTypeId( ), strCustomerId );

                DemandDashboard demandDashboard = new DemandDashboard( demand.getDemand( ).getUID( ) , false );
                demandDashboard.setStatus( demand.getStatus( ) );
                demandDashboard.setDemand( demand.getDemand( ) );                       
                
                if ( notificationList != null && notificationList.getNotifications( ) != null )
                {
                    demandDashboard.setListNotification( notificationList.getNotifications( ) );
                }
                listDemandDashboards.add( demandDashboard );
            }                
            listDemandDashboards = DemandDashboardHome.selectByDemandIds( listDemandDashboards );
        }
        return listDemandDashboards;
    }

    /**
     * Set model
     * @param model
     * @param nDefaultItemsPerPage
     * @param paginator
     * @param listDemandDashboards
     */
    private void setModel( Map<String, Object> model, int nDefaultItemsPerPage, LocalizedDelegatePaginator<DemandDisplay> paginator, List<DemandDashboard> listDemandDashboards )
    {
        model.put( MARK_DEMAND_TYPE_LIST, _notificationService.getListDemandType( ) );
        model.put( MARK_NB_ITEMS_PER_PAGE, nDefaultItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_LIST_DEMAND, listDemandDashboards );
    }
    
    /**
     * Returns the list of status ids that are compelted.
     * @return list of status ids that are completed.
     */
    private String getListStatusCompleted( )
    {
        StringBuilder listStatusInProgress = new StringBuilder( ); 
        for( EnumGenericStatus genericStatus : EnumGenericStatus.values( ) )
        {
            if( genericStatus.isFinalStatus( ) )
            {
                listStatusInProgress.append( genericStatus.getStatusId( ) + ",");
            }
        }       
        return listStatusInProgress.toString( );
    }
    
}