package evolutionmod.orbs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.actions.SuccubusGeneAction;
import evolutionmod.cards.AdaptableEvoCard;

public class LizardGene extends AbstractGene {
	public static final String ID = "evolutionmod:LizardGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public LizardGene() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new LizardGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
				m, p, new PoisonPower(m, p, poisonPerGene() * times), poisonPerGene() * times));
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new LizardAdaptation(1);
	}

	@Override
	public void updateDescription() {
		this.description = "#yPassive and #yEvoke: " + buildDescription();
	}

	private static String buildDescription() {
		return DESCRIPTION[0] + poisonPerGene() + DESCRIPTION[1];
	}
	private static int poisonPerGene() {
		int poison = 2;
		if (AbstractDungeon.player.hasPower("evolution:LizardForm")) {
			poison += AbstractDungeon.player.getPower("evolution:LizardForm").amount;
		}
		return poison;
	}

	private static class LizardAdaptation extends AdaptableEvoCard.AbstractAdaptation {

		LizardAdaptation(int amount) {
			super(amount);
		}

		@Override
		public void apply(AbstractPlayer p, AbstractMonster m) {
			LizardGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Lizard";
		}

		@Override
		public String getGeneId() {
			return ID;
		}

		@Override
		public AdaptableEvoCard.AbstractAdaptation makeCopy() {
			return new LizardAdaptation(this.amount);
		}
	}
}
