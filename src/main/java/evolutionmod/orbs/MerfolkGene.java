package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.cards.AdaptableEvoCard;

public class MerfolkGene extends AbstractGene {
	public static final String ID = "evolutionmod:MerfolkGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR_STRING = "[#5A5AFF]";
	public static final Color COLOR = new Color(0x5A5AFF00);
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final TooltipInfo TOOLTIP = new TooltipInfo(COLOR_STRING + NAME + "[]", "Orb: " + getOrbDescription());
	public static final String IMG_PATH = "evolutionmod/images/orbs/MermaidGene.png";
	public static final int BLOCK = 3;

	public MerfolkGene() {
		super(ID, NAME, getDescription(), IMG_PATH);
	}

	@Override
	public void onEndOfTurn() {
		super.onEndOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public void onEvoke() {
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new MerfolkGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		int block = blockPerGene();
		if (block > 0) {
			for (int i = 0; i < times; ++i) {
				AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
			}
		}
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = getOrbDescription();
	}

	public static String getOrbDescription() {
		return "At the #rend #rof #ryour #rturn and when #yEvoked: NL " + getDescription();
	}

	public static String getDescription() {
		return DESCRIPTION[0] + blockPerGene() + DESCRIPTION[1];
	}

	private static int blockPerGene() {
		return BLOCK;
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
			MerfolkGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "[#5252FF]Merfolk[]";
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
