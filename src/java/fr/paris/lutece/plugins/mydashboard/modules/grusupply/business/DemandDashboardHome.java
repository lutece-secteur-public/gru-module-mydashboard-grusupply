/*
 * Copyright (c) 2002-2023, Mairie de Paris
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
 
package fr.paris.lutece.plugins.mydashboard.modules.grusupply.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for DemandDashboard objects
 */

public final class DemandDashboardHome
{

    // Static variable pointed at the DAO instance
    private static IDemandDashboardDAO _dao = ( IDemandDashboardDAO ) SpringContextService.getBean( "mydashboard-grusupply.demandDashboardDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "mydashboard-grusupply" );

    /**
     * Private constructor - this class need not be instantiated
     */

    private DemandDashboardHome(  )
    {
    }

    /**
     * Create an instance of the demandDashboard class
     * @param demandDashboard The instance of the DemandDashboard which contains the informations to store
     * @return The  instance of demandDashboard which has been created with its primary key.
     */

    public static DemandDashboard create( DemandDashboard demandDashboard )
    {
        _dao.insert( demandDashboard, _plugin );

        return demandDashboard;
    }


    /**
     * Update of the demandDashboard which is specified in parameter
     * @param demandDashboard The instance of the DemandDashboard which contains the data to store
     * @return The instance of the  demandDashboard which has been updated
     */

    public static DemandDashboard update( DemandDashboard demandDashboard )
    {
        _dao.store( demandDashboard, _plugin );

        return demandDashboard;
    }


    /**
     * Remove the demandDashboard whose identifier is specified in parameter
     * @param nDemandDashboardId The demandDashboard Id
     */


    public static void remove( int nDemandDashboardId )
    {
        _dao.delete( nDemandDashboardId, _plugin );
    }


    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a demandDashboard whose identifier is specified in parameter
     * @param nKey The demandDashboard primary key
     * @return an instance of DemandDashboard
     */

    public static DemandDashboard findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }


    /**
     * Load the data of all the demandDashboard objects and returns them in form of a list
     * @return the list which contains the data of all the demandDashboard objects
     */

    public static List<DemandDashboard> getDemandDashboardsList( )
    {
        return _dao.selectDemandDashboardsList( _plugin );
    }

    /**
     * Load the data of all the demandDashboard objects by demand ids and returns them in form of a list
     * @param listDemandIds
     * @return the list which contains the data of all the demandDashboard objects
     */

    public static List<DemandDashboard> selectByDemandIds( List<DemandDashboard> listDemandDashboards )
    {
        return _dao.selectByDemandIds( listDemandDashboards, _plugin );
    }
    
}