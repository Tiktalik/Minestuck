package com.mraof.minestuck.tileentity;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.block.BlockComputerOn;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.util.UsernameHandler.PlayerIdentifier;

public class TileEntityComputer extends TileEntity
{

	/**
	 * 0 = client, 1 = server
	 */
	public Hashtable<Integer, Boolean> installedPrograms = new Hashtable<Integer, Boolean>();
	public GuiComputer gui;
	public PlayerIdentifier owner;
	@SideOnly(Side.CLIENT)
	public int ownerId;
	public Hashtable<Integer, String> latestmessage = new Hashtable<Integer, String>();
	public NBTTagCompound programData = new NBTTagCompound();
	public int programSelected = -1;

	@SideOnly(Side.CLIENT)
	public ComputerProgram program;

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) 
	{
		super.readFromNBT(par1NBTTagCompound);	
		if (par1NBTTagCompound.getCompoundTag("programs") != null) 
		{
			NBTTagCompound programs = par1NBTTagCompound.getCompoundTag("programs");
			for (Object name : programs.getKeySet()) 
			{
				installedPrograms.put(programs.getInteger((String)name), true);
			}
		}

		latestmessage.clear();
		for(Entry<Integer,Boolean> e : installedPrograms.entrySet())
			if(e.getValue())
				latestmessage.put(e.getKey(), par1NBTTagCompound.getString("text" + e.getKey()));

		programData = par1NBTTagCompound.getCompoundTag("programData");

		if(!par1NBTTagCompound.hasKey("programData")) 
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("connectedToServer", par1NBTTagCompound.getBoolean("connectServer"));
			nbt.setBoolean("isResuming", par1NBTTagCompound.getBoolean("resumeClient"));
			programData.setTag("program_0", nbt);
			nbt = new NBTTagCompound();
			nbt.setString("connectedClient", par1NBTTagCompound.getString("connectClient"));
			nbt.setBoolean("isOpen", par1NBTTagCompound.getBoolean("serverOpen"));
			programData.setTag("program_1", nbt);
		}
		if(par1NBTTagCompound.hasKey("ownerId"))
			ownerId = par1NBTTagCompound.getInteger("ownerId");
		else this.owner = UsernameHandler.load(par1NBTTagCompound, "owner");
		if(gui != null)
			gui.updateGui();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) 
	{
		super.writeToNBT(par1NBTTagCompound);
		NBTTagCompound programs = new NBTTagCompound();
		Iterator<Entry<Integer, Boolean>> it = this.installedPrograms.entrySet().iterator();
		//int place = 0;
		while (it.hasNext()) 
		{
			Map.Entry<Integer, Boolean> pairs = it.next();
			int program = pairs.getKey();
			programs.setInteger("program" + program,program);
			//place++;
		}
		for(Entry<Integer, String> e : latestmessage.entrySet())
			par1NBTTagCompound.setString("text" + e.getKey(), e.getValue());
		par1NBTTagCompound.setTag("programs",programs);
		par1NBTTagCompound.setTag("programData", (NBTTagCompound) programData.copy());
		if (owner != null) 
			owner.saveToNBT(par1NBTTagCompound, "owner");
	}

	@Override
	public Packet getDescriptionPacket() 
	{
		NBTTagCompound tagCompound = new NBTTagCompound();
		this.writeToNBT(tagCompound);
		tagCompound.removeTag("owner");
		tagCompound.removeTag("ownerMost");
		tagCompound.removeTag("ownerLeast");
		tagCompound.setInteger("ownerId", owner.getId());
		if(hasProgram(1))
		{
			SburbConnection c = SkaianetHandler.getServerConnection(ComputerData.createData(this));
			if(c != null)
				tagCompound.getCompoundTag("programData").getCompoundTag("program_1").setInteger("connectedClient", c.getClientIdentifier().getId());
		}
		return new S35PacketUpdateTileEntity(this.pos, 2, tagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	public boolean hasProgram(int id) 
	{
		if(id == -1)
		{
			IBlockState state = worldObj.getBlockState(pos);
			return state.getBlock() == MinestuckBlocks.blockMachine ? (Boolean) state.getValue(BlockComputerOn.BSOD) : false;
		}
		return installedPrograms.get(id) == null ? false:installedPrograms.get(id);
	}

	public NBTTagCompound getData(int id) 
	{
		if(!programData.hasKey("program_"+id))
			programData.setTag("program_" + id, new NBTTagCompound());
		return programData.getCompoundTag("program_" + id);
	}

	public void closeAll() 
	{
		for(Entry<Integer, Boolean> entry : installedPrograms.entrySet())
			if(entry.getValue() && entry.getKey() != -1 && ComputerProgram.getProgram(entry.getKey()) != null)
				ComputerProgram.getProgram(entry.getKey()).onClosed(this);
	}

	public void connected(PlayerIdentifier player, boolean isClient)
	{
		if(isClient)
		{
			getData(0).setBoolean("isResuming", false);
			getData(0).setBoolean("connectedToServer", true);
		}
		else
		{
			this.getData(1).setBoolean("isOpen", false);
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}
	
}
