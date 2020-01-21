package evolutionmod.orbs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.LymeanGeneAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.LymeanFormPower;

public class LymeanGeneV2 extends AbstractGene {
	public static final String ID = "evolutionmod:LymeanGeneV2";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public LymeanGeneV2() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new LymeanGeneV2();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		AbstractDungeon.actionManager.addToTop(
				new LymeanGeneAction(p, null, blockPerGene() * times, healPerGene() * times));
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = "#yPassive: " + buildDescription();
	}

	private static String buildDescription() {
//		return DESCRIPTION[0] + healPerGene() + DESCRIPTION[1] + exhaustPerGene() + DESCRIPTION[2];
		return DESCRIPTION[0] + blockPerGene() + DESCRIPTION[1] + healPerGene() + DESCRIPTION[2];
	}

	private static int healPerGene() {
		return 2;
	}

	private static int blockPerGene() {
		int block = 5;
		if (AbstractDungeon.player.hasPower(LymeanFormPower.POWER_ID)) {
			block += AbstractDungeon.player.getPower(LymeanFormPower.POWER_ID).amount;
		}
		return block;
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
			LymeanGeneV2.apply(p, m, this.amount);
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
