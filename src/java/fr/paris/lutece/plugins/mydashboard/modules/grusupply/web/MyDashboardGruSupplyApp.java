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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import fr.paris.lutece.plugins.grubusiness.business.web.rs.NotificationResult;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.DemandDashboard;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.DemandDashboardHome;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.service.NotificationGruService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * MyDashboardGruSupplyApp
 *
 */
@Controller( xpageName = "myDashboardGruSupplyApp", pageTitleI18nKey = "mydashboard-grusupply.pageTitle", pagePathI18nKey = "mydashboard-grusupply.pagePathLabel" )
public class MyDashboardGruSupplyApp extends MVCApplication
{

    /**
     * 
     */
    private static final long      serialVersionUID              = 1L;

    // TEMPLATE
    private static final String    TEMPLATE_NOTIFICATION_CONTENT = "skin/plugins/mydashboard/modules/grusupply/dashboard_notification_content.html";

    // VIEW
    private static final String    VIEW_LIST_NOTIFICATION        = "getNotificationContent";

    // PARAMETER
    private static final String    PARAMTER_DEMAND_ID            = "demandId";
    private static final String    PARAMTER_DEMAND_TYPE_ID       = "demandTypeId";

    // MARKER
    private static final String    MARK_NOTIFICATION_LIST        = "notificationContentList";
    private static final String    MARK_JSON_CONTENT             = "content";

    // SERVICE
    private NotificationGruService _notificationService          = SpringContextService.getBean( NotificationGruService.BEAN_NAME );

    @View( VIEW_LIST_NOTIFICATION )
    public XPage getNotificationContent( HttpServletRequest request )
    {
        String strIdDemand = request.getParameter( PARAMTER_DEMAND_ID );
        String strIdDemandType = request.getParameter( PARAMTER_DEMAND_TYPE_ID );

        LuteceUser user = getRegistredUser( request );

        if ( StringUtils.isNotEmpty( strIdDemand ) && user != null )
        {
            NotificationResult result = _notificationService.getListNotification( strIdDemand, strIdDemandType, user.getName( ) );

            Map<String, Object> model = getModel( );

            if ( result != null && result.getNotifications( ) != null )
            {
                model.put( MARK_NOTIFICATION_LIST, result.getNotifications( ) );
                
                //Demand is read
                if( DemandDashboardHome.findByPrimaryKey( Integer.parseInt( strIdDemand ) ) == null )
                {
                    DemandDashboardHome.create( new DemandDashboard( Integer.parseInt( strIdDemand ), true ) );
                }
            }

            HtmlTemplate htmlTemplate = AppTemplateService.getTemplate( TEMPLATE_NOTIFICATION_CONTENT, request.getLocale( ), model );

            JSONObject json = new JSONObject( );
            json.put( MARK_JSON_CONTENT, htmlTemplate.getHtml( ) );

            return responseJSON( json.toString( ) );
        }

        return responseJSON( StringUtils.EMPTY );
    }

}
