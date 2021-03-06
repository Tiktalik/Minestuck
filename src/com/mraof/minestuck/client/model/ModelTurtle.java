// Date: 1/8/2017 8:40:00 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX
package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTurtle extends ModelBase
{
	// fields
	ModelRenderer head;
	ModelRenderer leg1;
	ModelRenderer leg2;
	ModelRenderer tail;
	ModelRenderer body;
	ModelRenderer shell;
	ModelRenderer shellRim;
	ModelRenderer nose;
	
	public ModelTurtle()
	{
		textureWidth = 64;
		textureHeight = 64;
		
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-2F, -4F, -4F, 4, 4, 5);
		head.setRotationPoint(-0.5F, 12F, 1F);
		head.setTextureSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		leg1 = new ModelRenderer(this, 32, 0);
		leg1.addBox(-1F, 0F, -1F, 2, 4, 2);
		leg1.setRotationPoint(1F, 20F, 1F);
		leg1.setTextureSize(64, 64);
		leg1.mirror = true;
		setRotation(leg1, 0F, 0F, 0F);
		leg2 = new ModelRenderer(this, 32, 6);
		leg2.addBox(-1F, 0F, -1F, 2, 4, 2);
		leg2.setRotationPoint(-2F, 20F, 1F);
		leg2.setTextureSize(64, 64);
		leg2.mirror = true;
		setRotation(leg2, 0F, 0F, 0F);
		tail = new ModelRenderer(this, 48, 0);
		tail.addBox(-1F, -1F, 0F, 3, 4, 1);
		tail.setRotationPoint(-1F, 20F, 2F);
		tail.setTextureSize(64, 64);
		tail.mirror = true;
		setRotation(tail, 0.2974289F, 0F, 0F);
		body = new ModelRenderer(this, 0, 52);
		body.addBox(-3F, -1F, -2F, 5, 4, 8);
		body.setRotationPoint(0F, 18F, -1F);
		body.setTextureSize(64, 64);
		body.mirror = true;
		setRotation(body, 1.570796F, 0F, 0F);
		shell = new ModelRenderer(this, 0, 18);
		shell.addBox(-2F, -1F, -4F, 5, 2, 7);
		shell.setRotationPoint(-1F, 15F, 4F);
		shell.setTextureSize(64, 64);
		shell.mirror = true;
		setRotation(shell, 1.570796F, 0F, 0F);
		shellRim = new ModelRenderer(this, 0, 34);
		shellRim.addBox(-3F, -1F, -3F, 6, 1, 8);
		shellRim.setRotationPoint(-0.5F, 16.5F, 3F);
		shellRim.setTextureSize(64, 64);
		shellRim.mirror = true;
		setRotation(shellRim, 1.570796F, 0F, 0F);
		nose = new ModelRenderer(this, 48, 5);
		nose.addBox(-1F, -1F, -1F, 3, 2, 1);
		nose.setRotationPoint(-1.1F, 10.5F, -3F);
		nose.setTextureSize(64, 64);
		nose.mirror = true;
		setRotation(nose, 0F, 0F, 0F);
	}
	
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale)
	{
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		head.render(scale);
		leg1.render(scale);
		leg2.render(scale);
		tail.render(scale);
		body.render(scale);
		shell.render(scale);
		shellRim.render(scale);
		nose.render(scale);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}