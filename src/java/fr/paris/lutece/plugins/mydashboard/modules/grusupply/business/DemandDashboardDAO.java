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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Statement;

/**
 * This class provides Data Access methods for DemandDashboard objects
 */
public final class DemandDashboardDAO implements IDemandDashboardDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT               = "SELECT demand_id, is_read FROM mydashboard_grusupply_demand WHERE demand_id = ?";
    private static final String SQL_QUERY_INSERT               = "INSERT INTO mydashboard_grusupply_demand ( demand_id, is_read ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE               = "DELETE FROM mydashboard_grusupply_demand WHERE demand_id = ? ";
    private static final String SQL_QUERY_UPDATE               = "UPDATE mydashboard_grusupply_demand SET demand_id = ?, is_read = ? WHERE demand_id = ?";
    private static final String SQL_QUERY_SELECTALL            = "SELECT demand_id, is_read FROM mydashboard_grusupply_demand";
    private static final String SQL_QUERY_SELECT_BY_DEMAND_IDS = "SELECT demand_id, is_read FROM mydashboard_grusupply_demand WHERE demand_id IN (%s)";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( DemandDashboard demandDashboard, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {

            int nIndex = 0;
            daoUtil.setInt( ++nIndex, demandDashboard.getDemandId( ) );
            daoUtil.setBoolean( ++nIndex, demandDashboard.getRead( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DemandDashboard load( int nId, Plugin plugin )
    {
        DemandDashboard demandDashboard = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                demandDashboard = new DemandDashboard( );

                demandDashboard.setDemandId( daoUtil.getInt( "demand_id" ) );
                demandDashboard.setRead( daoUtil.getBoolean( "is_read" ) );
            }

        }
        return demandDashboard;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int ndemandDashboardId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, ndemandDashboardId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( DemandDashboard demandDashboard, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {

            int nIndex = 0;
            daoUtil.setInt( ++nIndex, demandDashboard.getDemandId( ) );
            daoUtil.setBoolean( ++nIndex, demandDashboard.getRead( ) );
            daoUtil.setInt( ++nIndex, demandDashboard.getDemandId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DemandDashboard> selectDemandDashboardsList( Plugin plugin )
    {
        List<DemandDashboard> listDemandDashboards = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                DemandDashboard demandDashboard = new DemandDashboard( );
                demandDashboard.setDemandId( daoUtil.getInt( "demand_id" ) );
                demandDashboard.setRead( daoUtil.getBoolean( "is_read" ) );
                listDemandDashboards.add( demandDashboard );
            }

        }
        return listDemandDashboards;
    }

    @Override
    public List<DemandDashboard> selectByDemandIds( List<DemandDashboard> listDemandDashboards, Plugin plugin )
    {

        if ( listDemandDashboards.isEmpty( ) )
            return listDemandDashboards;

        String sql = String.format( SQL_QUERY_SELECT_BY_DEMAND_IDS, listDemandDashboards.stream( ).map( v -> "?" ).collect( Collectors.joining( ", " ) ) );

        try ( DAOUtil daoUtil = new DAOUtil( sql, plugin ) )
        {
            int index = 1;
            for ( DemandDashboard demandDashboard : listDemandDashboards )
            {
                daoUtil.setInt( index++, demandDashboard.getDemandId( ) );
            }

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                for ( DemandDashboard demandDashboard : listDemandDashboards )
                {
                    if( demandDashboard.getDemandId( ) == daoUtil.getInt( "demand_id" ) )
                    {
                        demandDashboard.setDemandId( daoUtil.getInt( "demand_id" ) );
                        demandDashboard.setRead( daoUtil.getBoolean( "is_read" ) );
                    }
                }
            }

        }
        return listDemandDashboards;
    }

}