package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.FateAction;
import evolutionmod.actions.TriggerScryEffectsAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.PotencyPower;

import java.util.List;

public class LymeanGene extends AbstractGene {
	public static final String ID = "evolutionmod:LymeanGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR = "[#50FFFF]";
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/LymeanGene.png";
	public static final int SCRY = 2;

	public LymeanGene() {
		super(ID, NAME, getDescription(), IMG_PATH, COLOR);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new LymeanGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		int fate = fate();
		if (fate > 0) {
			for (int i = 0; i < times; ++i) {
				AbstractDungeon.actionManager.addToBottom(new FateAction(fate));
//				AbstractDungeon.actionManager.addToBottom(new TriggerScryEffectsAction());
			}
		}
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	@Override
	public void updateDescription() {
		this.description = "#yPassive: " + getDescription();
	}

	public static List<TooltipInfo> addTooltip(List<TooltipInfo> tooltips, String rawDescription) {
		if (rawDescription.contains("Lymean")) {
			tooltips.add(new TooltipInfo(
					COLOR + NAME + "[]",
					getDescription()));
		}
		return tooltips;
	}

	public static String getDescription() {
//		return DESCRIPTION[0] + blockPerGene() + DESCRIPTION[1] + healPerGene() + DESCRIPTION[2];
		return DESCRIPTION[0] + fate();
	}

//	private static int healPerGene() {
//		return 2;
//	}
//
//	private static int blockPerGene() {
//		int block = 5;
//		if (AbstractDungeon.player.hasPower(PotencyPower.POWER_ID)) {
//			block += AbstractDungeon.player.getPower(PotencyPower.POWER_ID).amount;
//		}
//		return block;
//	}

	private static int fate() {
		int scry = SCRY;
		if (CardCrawlGame.isInARun()) {
			if (AbstractDungeon.player.hasPower(PotencyPower.POWER_ID)) {
				scry += AbstractDungeon.player.getPower(PotencyPower.POWER_ID).amount;
			}
		}
		return scry;
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
			LymeanGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Lymean";
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
