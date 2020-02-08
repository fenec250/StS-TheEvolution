package evolutionmod.orbs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.ChargePower;

public class CentaurGene extends AbstractGene {
	public static final String ID = "evolutionmod:CentaurGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/CentaurGene.png";

	public CentaurGene() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new CentaurGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		int chargeToApply = chargeToApply() * times;
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ChargePower(p, chargeToApply), chargeToApply));
	}

	@Override
	public void updateDescription() {
		this.description = "#yPassive and #yEvoke: " + buildDescription();
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	private static String buildDescription() {
		return DESCRIPTION[0] + chargeToApply() + DESCRIPTION[1];
	}

	private static int chargeToApply() {
		int chargeToApply = 2;
//		if (AbstractDungeon.player.hasPower("evolution:CentaurForm")) {
//			chargeToApply += AbstractDungeon.player.getPower("evolution:CentaurForm").amount;
//		}
		return chargeToApply;
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
			CentaurGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "[#FF8040]Centaur[]";
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
