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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.grubusiness.business.demand.DemandDisplayDTO;
import fr.paris.lutece.plugins.grusupply.web.rs.DemandResult;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.DemandDashboard;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.DemandDashboardHome;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.service.NotificationGruService;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * MyDashboardComponentLastNotificationGRU
 *
 */
public class MyDashboardComponentLastNotificationGRU extends MyDashboardComponent
{

    /**
     * 
     */
    private static final long      serialVersionUID                = 8297192924908575568L;

    private static final String    TEMPLATE_LAST_NOTIFICATION_LIST = "skin/plugins/mydashboard/modules/grusupply/dashboard_last_demand.html";
    private static final String    DASHBOARD_COMPONENT_ID          = "mydashboard-grusupply.componentLastNotification";
    private static final String    MESSAGE_COMPONENT_DESCRIPTION   = "module.mydashboard.grusupply.myDashboardComponentLastNotification.description";

    // MARKS
    private static final String    MARK_DEMAND_TYPE_LIST           = "demand_types_list";
    private static final String    MARK_LIST_DEMAND                = "list_demands";

    @Inject
    @Named( NotificationGruService.BEAN_NAME )
    private NotificationGruService _notificationService;

    @Override
    public String getDashboardData( HttpServletRequest request )
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        if ( user != null )
        {
            Map<String, Object> model = new HashMap<>( );
            
            DemandResult demandResult = _notificationService.getListDemand( user.getName( ), "1" );
            List<DemandDashboard> listDemandDashboards = new ArrayList<>( );
            
            if ( demandResult != null && CollectionUtils.isNotEmpty( demandResult.getListDemandDisplay( ) ) )
            {
                for( DemandDisplayDTO demand : demandResult.getListDemandDisplay( ) )
                {
                    DemandDashboard demandDashboard = new DemandDashboard( demand.getDemand( ).getDemandId( ) , false );
                    demandDashboard.setDemand( demand.getDemand( ) );
                    demandDashboard.setStatus( demand.getStatus( ) );
                    listDemandDashboards.add( demandDashboard );
                }                
                listDemandDashboards = DemandDashboardHome.selectByDemandIds( listDemandDashboards );
            }

            model.put( MARK_DEMAND_TYPE_LIST, _notificationService.getListDemandType( ) );
            model.put( MARK_LIST_DEMAND, listDemandDashboards );

            HtmlTemplate htmTemplate = AppTemplateService.getTemplate( TEMPLATE_LAST_NOTIFICATION_LIST, request.getLocale( ), model );

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

}
