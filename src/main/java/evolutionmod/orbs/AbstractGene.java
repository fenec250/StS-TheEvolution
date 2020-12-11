package evolutionmod.orbs;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import evolutionmod.cards.AdaptableEvoCard;

public abstract class AbstractGene extends CustomOrb {

	private String colorString;
	private boolean showOrbText;

	public AbstractGene(String id, String name, String description, String imgPath, String color) {
		this(id, name, description, imgPath, color, true);
	}

	public AbstractGene(String id, String name, String description, String imgPath, String color, boolean showOrbText) {
		super(id, name, 1, 1, description, description, imgPath);
		this.colorString = color;
		this.showOrbText = showOrbText;
		this.channelAnimTimer = 0.3F;
	}

	@Override
	public void onEvoke() {
		onStartOfTurn();
		onEndOfTurn();
	}

	public abstract AdaptableEvoCard.AbstractAdaptation getAdaptation();
	public String getColoredName() {
		return colorize(this.name);
	}
	public String colorize(String string) {
		return colorString + string + "[]";
	}

	@Override
	protected void renderText(SpriteBatch sb) {
//		 disable showing an amount
		if (showOrbText) {
			super.renderText(sb);
		}
	}
}
