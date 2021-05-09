package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.RagePower;
import evolutionmod.cards.AdaptableEvoCard;

public class BeastGene extends AbstractGene {
	public static final String ID = "evolutionmod:BeastGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR_STRING = "[#B06060]";
	public static final Color COLOR = new Color(0xB0606000);
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final TooltipInfo TOOLTIP = new TooltipInfo(COLOR_STRING + NAME + "[]", "Orb: " + getOrbDescription());
	public static final String IMG_PATH = "evolutionmod/images/orbs/BeastGene.png";
	public static final int RAGE = 2;

	public BeastGene() {
		super(ID, NAME, getDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1, false);
	}

	@Override
	public void onEvoke() {
		apply(AbstractDungeon.player, null, 1, true);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new BeastGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times, boolean addToTop) {
//		int block = block();
//		if (block > 0) {
//			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, block));
//		}
		int rage = rage() * times;
		if (addToTop) {
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new RagePower(p, rage)));
		} else {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RagePower(p, rage)));
		}
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = getOrbDescription();
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	public static String getOrbDescription() {
		return "At the #bstart #bof #byour #bturn and when #yEvoked: NL " + getDescription();
	}

	public static String getDescription() {
		return DESCRIPTION[0] + rage() + DESCRIPTION[1];
	}

	private static int rage() {
		return RAGE;
	}

	public static class Adaptation extends AdaptableEvoCard.AbstractAdaptation {

		public Adaptation(int amount) {
			super(amount);
		}
		public Adaptation(int amount, int max) {
			super(amount, max);
		}
		Adaptation(Adaptation adaptation) {
			super(adaptation);
		}

		@Override
		public void apply(AbstractPlayer p, AbstractMonster m) {
			BeastGene.apply(p, m, this.amount, true);
		}

		@Override
		public String text() {
			return "Beast";
		}

		@Override
		public String getGeneId() {
			return ID;
		}

		@Override
		public AdaptableEvoCard.AbstractAdaptation makeCopy() {
			return new Adaptation(this);
		}
	}
}
