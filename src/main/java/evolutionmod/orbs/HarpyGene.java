package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.cards.AdaptableEvoCard;

import java.util.List;

public class HarpyGene extends AbstractGene {
	public static final String ID = "evolutionmod:HarpyGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR = "[#FFFF80]";
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/HarpyGene.png";
	public static final int DRAW = 1;

	public HarpyGene() {
		super(ID, NAME, getDescription(), IMG_PATH, COLOR);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new HarpyGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, times));
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = "#yPassive: " + getDescription();
	}

	public static List<TooltipInfo> addTooltip(List<TooltipInfo> tooltips, String rawDescription) {
		if (rawDescription.contains("Harpy")) {
			tooltips.add(new TooltipInfo(
					COLOR + NAME + "[]",
					getDescription()));
		}
		return tooltips;
	}

	public static String getDescription() {
		return DESCRIPTION[0] + DESCRIPTION[1];
//		int draw = draw();
//		return DESCRIPTION[0] + (draw == 1 ? DESCRIPTION[1] : draw + DESCRIPTION[2]);
	}

	private static int draw() {
		return DRAW;
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
			HarpyGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "[#FFFF80]Harpy[]";
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
