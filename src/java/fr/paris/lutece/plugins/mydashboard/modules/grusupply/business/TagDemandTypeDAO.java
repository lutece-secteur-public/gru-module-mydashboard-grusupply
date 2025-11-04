/*
 * Copyright (c) 2002-2025, Mairie de Paris
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

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import java.sql.Statement;

/**
 * This class provides Data Access methods for TagDemandType objects
 */
public final class TagDemandTypeDAO implements ITagDemandTypeDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT    = "SELECT id, id_demand_type, tag FROM mydashboard_grusupply_tag_demand_type WHERE id = ?";
    private static final String SQL_QUERY_INSERT    = "INSERT INTO mydashboard_grusupply_tag_demand_type ( id_demand_type, tag ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE    = "DELETE FROM mydashboard_grusupply_tag_demand_type WHERE id = ? ";
    private static final String SQL_QUERY_UPDATE    = "UPDATE mydashboard_grusupply_tag_demand_type SET id = ?, id_demand_type = ?, tag = ? WHERE id = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id, id_demand_type, tag FROM mydashboard_grusupply_tag_demand_type";
    private static final String SQL_QUERY_SELECT_BY_ID_DEMAND_TYPE = "SELECT id, id_demand_type, tag FROM mydashboard_grusupply_tag_demand_type WHERE id_demand_type = ? ";
    private static final String SQL_QUERY_DELETE_BY_ID_DEMAND_TYPE = "DELETE FROM mydashboard_grusupply_tag_demand_type WHERE id_demand_type = ? ";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( TagDemandType tagDemandType )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, tagDemandType.getIdDemandType( ) );
            daoUtil.setString( ++nIndex, tagDemandType.getTag( ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                tagDemandType.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public TagDemandType load( int nId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            TagDemandType tagDemandType = null;

            if ( daoUtil.next( ) )
            {
                tagDemandType = new TagDemandType( );

                tagDemandType.setId( daoUtil.getInt( "id" ) );
                tagDemandType.setIdDemandType( daoUtil.getInt( "id_demand_type" ) );
                tagDemandType.setTag( daoUtil.getString( "tag" ) );
            }

            return tagDemandType;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nTagDemandTypeId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nTagDemandTypeId );
            daoUtil.executeUpdate( );
        }
    }
    
    @Override
    public void deleteByIdDemandType( int nIdDemandType )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ID_DEMAND_TYPE ) )
        {
            daoUtil.setInt( 1, nIdDemandType );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( TagDemandType tagDemandType )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, tagDemandType.getId( ) );
            daoUtil.setInt( ++nIndex, tagDemandType.getIdDemandType( ) );
            daoUtil.setString( ++nIndex, tagDemandType.getTag( ) );
            daoUtil.setInt( ++nIndex, tagDemandType.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<TagDemandType> selectTagDemandTypesList( )
    {
        List<TagDemandType> listTagDemandTypes = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                TagDemandType tagDemandType = new TagDemandType( );
                tagDemandType.setId( daoUtil.getInt( "id" ) );
                tagDemandType.setIdDemandType( daoUtil.getInt( "id_demand_type" ) );
                tagDemandType.setTag( daoUtil.getString( "tag" ) );
                listTagDemandTypes.add( tagDemandType );
            }

            return listTagDemandTypes;
        }
    }

    @Override
    public List<TagDemandType> selectTagDemandTypesList( int nIdDemandType )
    {
        List<TagDemandType> listTagDemandTypes = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID_DEMAND_TYPE ) )
        {
            daoUtil.setInt( 1, nIdDemandType );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                TagDemandType tagDemandType = new TagDemandType( );
                tagDemandType.setId( daoUtil.getInt( "id" ) );
                tagDemandType.setIdDemandType( daoUtil.getInt( "id_demand_type" ) );
                tagDemandType.setTag( daoUtil.getString( "tag" ) );
                listTagDemandTypes.add( tagDemandType );
            }

            return listTagDemandTypes;
        }
    }

}
