package am2.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import am2.api.compendium.CompendiumEntry;
import am2.client.gui.controls.GuiButtonCompendiumNext;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiArcaneCompendium extends GuiScreen {
	
	private static final ResourceLocation background = new ResourceLocation("arsmagica2", "textures/gui/ArcaneCompendiumIndexGui.png");
	private static final int xSize = 360;
	private static final int ySize = 256;
	
	public CompendiumEntry entry;
	private int page = 0;
	private GuiButtonCompendiumNext nextPage;
	private GuiButtonCompendiumNext prevPage;

    public GuiArcaneCompendium(CompendiumEntry entry) {
		this.entry = entry;
	}
	
	@Override
	public void initGui() {
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		int idCount = 0;
		prevPage = new GuiButtonCompendiumNext(idCount++, l + 35, i1 + ySize - 25, false);
		nextPage = new GuiButtonCompendiumNext(idCount, l + 315, i1 + ySize - 25, true);
		
		prevPage.visible = false;
		nextPage.visible = false;
		
		buttonList.add(nextPage);
		buttonList.add(prevPage);
		buttonList.add(new GuiButtonCompendiumNext(-1, l + 20, i1 + 15, false));

		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		

		GlStateManager.color(1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(background);
		this.drawTexturedModalRect_Classic(l, i1, 0, 0, xSize, ySize, 256, 240);

		RenderHelper.disableStandardItemLighting();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, -1);
		//GlStateManager.disableDepth();
		//this.drawDefaultBackground();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();


		RenderHelper.enableStandardItemLighting();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
	}
	
	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	public void drawTexturedModalRect_Classic(int dst_x, int dst_y, int src_x, int src_y, int dst_width, int dst_height, int src_width, int src_height){
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;

		Tessellator var9 = Tessellator.getInstance();
		var9.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
		var9.getBuffer().pos(dst_x + 0, dst_y + dst_height, this.zLevel).tex((src_x + 0) * var7, (src_y + src_height) * var8).endVertex();
		var9.getBuffer().pos(dst_x + dst_width, dst_y + dst_height, this.zLevel).tex((src_x + src_width) * var7, (src_y + src_height) * var8).endVertex();
		var9.getBuffer().pos(dst_x + dst_width, dst_y + 0, this.zLevel).tex((src_x + src_width) * var7, (src_y + 0) * var8).endVertex();
		var9.getBuffer().pos(dst_x + 0, dst_y + 0, this.zLevel).tex((src_x + 0) * var7, (src_y + 0) * var8).endVertex();
		var9.draw();
	}
}
