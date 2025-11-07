/*
 * Copyright (c) 2002-2025, City of Paris
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.grubusiness.business.notification.EnumNotificationType;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.DemandResult;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.service.NotificationGruService;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.service.TagDemandTypeService;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.util.GrusupplyConstants;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.util.MydashboardGrusupplyUtil;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * MyDashboardComponentLastAppointments
 *
 */
public class MyDashboardComponentLastAppointments extends MyDashboardComponent
{
    /**
     * 
     */
    private static final long serialVersionUID = 6196585897437398381L;

    private static final String    TEMPLATE_LAST_NOTIFICATION_LIST = "skin/plugins/mydashboard/modules/grusupply/dashboard_last_demand_appointments.html";
    private static final String    DASHBOARD_COMPONENT_ID          = "mydashboard-grusupply.componentLastAppointments";
    private static final String    MESSAGE_COMPONENT_DESCRIPTION   = "module.mydashboard.grusupply.myDashboardComponentLastAppointments.description";
    
    @Override
    public String getDashboardData( HttpServletRequest request )
    {
        final LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        if ( user == null )
        {
            return StringUtils.EMPTY;
        }

        Map<String, Object> model = new HashMap<>( );            
        final String strCustomerId = MydashboardGrusupplyUtil.getCustomerId(user.getName(),request);
        
        String strIdsdemandType = TagDemandTypeService.getInstance( ).getListDemandTypeByTag( 
                GrusupplyConstants.PROPERTY_TAG_RDV, null )
                .stream( )
                .map( demandType -> String.valueOf( demandType.getIdDemandType( ) ) )
                .collect( Collectors.joining(","));
        
        DemandResult demandResult = NotificationGruService.getInstance( ).getListDemandByStatus(
                strCustomerId,
                MydashboardGrusupplyUtil.getListStatus( GrusupplyConstants.PROPERTY_TAG_RDV, true, true ),
                strIdsdemandType, 
                "1", 
                GrusupplyConstants.PROPERTY_LIMIT_NOTIFICATION_RESULT, 
                EnumNotificationType.MYDASHBOARD.toString( ), 
                null
        );
                   
        model.put( GrusupplyConstants.MARK_LIST_DEMAND, MydashboardGrusupplyUtil.getDemandDashboards(strCustomerId, demandResult));
        model.put( GrusupplyConstants.MARK_DEMAND_TYPE_LIST, NotificationGruService.getInstance( ).getListDemandType( ) );

        HtmlTemplate htmTemplate = AppTemplateService.getTemplate( TEMPLATE_LAST_NOTIFICATION_LIST, request.getLocale( ), model );
        return htmTemplate.getHtml( );
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
