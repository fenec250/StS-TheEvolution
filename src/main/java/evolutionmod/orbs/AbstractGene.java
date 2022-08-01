package evolutionmod.orbs;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import evolutionmod.cards.AdaptableEvoCard;

public abstract class AbstractGene extends CustomOrb {

	private boolean showOrbText;

	public AbstractGene(String id, String name, String description, String imgPath) {
		this(id, name, description, imgPath, false);
	}

	public AbstractGene(String id, String name, String description, String imgPath, boolean showOrbText) {
		super(id, name, 1, 1, description, description, imgPath);
		this.showOrbText = showOrbText;
		this.channelAnimTimer = 0.3F;
	}

	@Override
	public void onEvoke() {
		onStartOfTurn();
		onEndOfTurn();
	}

	public AbstractGameAction getChannelAction() {
		return new ChannelAction(this);
	}

	public abstract AdaptableEvoCard.AbstractAdaptation getAdaptation();

	@Override
	protected void renderText(SpriteBatch sb) {
//		 disable showing an amount
		if (showOrbText) {
			super.renderText(sb);
		}
	}
}
