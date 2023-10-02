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

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;

/**
 * This is the business class for the object DemandDashboard
 */
public class DemandDashboard
{
    // Variables declarations
    private int     _nDemandId;
    private boolean _bRead;
    private Demand _demand;
    private String _strStatus;

    /**
     * Constructor
     * 
     * @param nDemandId
     * @param bRead
     */
    public DemandDashboard( )
    {
    }

    /**
     * Constructor
     * 
     * @param nDemandId
     * @param bRead
     */
    public DemandDashboard( int nDemandId, boolean bRead )
    {
        _nDemandId = nDemandId;
        _bRead = bRead;
    }

    /**
     * Returns the DemandId
     * 
     * @return The DemandId
     */
    public int getDemandId( )
    {
        return _nDemandId;
    }

    /**
     * Sets the DemandId
     * 
     * @param nDemandId
     *            The DemandId
     */
    public void setDemandId( int nDemandId )
    {
        _nDemandId = nDemandId;
    }

    /**
     * Returns the Read
     * 
     * @return The Read
     */
    public boolean getRead( )
    {
        return _bRead;
    }

    /**
     * Sets the Read
     * 
     * @param bRead
     *            The Read
     */
    public void setRead( boolean bRead )
    {
        _bRead = bRead;
    }

    /**
     * @return the demand
     */
    public Demand getDemand( )
    {
        return _demand;
    }

    /**
     * @param demand the demand to set
     */
    public void setDemand( Demand demand )
    {
        this._demand = demand;
    }

    /**
     * @return the _strStatus
     */
    public String getStatus( )
    {
        return _strStatus;
    }

    /**
     * @param strStatus the _strStatus to set
     */
    public void setStatus( String strStatus )
    {
        this._strStatus = strStatus;
    }
    
    

}