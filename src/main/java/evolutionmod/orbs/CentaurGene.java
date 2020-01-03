package evolutionmod.orbs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import evolutionmod.cards.AdaptableEvoCard;

public class CentaurGene extends AbstractGene {
	public static final String ID = "evolutionmod:CentaurGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public CentaurGene() {
		super(ID, NAME, "first", IMG_PATH);
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
		int strengthToApply = strengthToApply() * times;
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, strengthToApply), strengthToApply));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseStrengthPower(p, strengthToApply), strengthToApply));
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = "#yPassive: " + buildDescription();
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new CentaurAdaptation(1);
	}

	private static String buildDescription() {
		int strength = strengthToApply();
		return DESCRIPTION[0] + strength + DESCRIPTION[1];
	}

	private static int strengthToApply() {
		int strengthToApply = 1;
		if (AbstractDungeon.player.hasPower("evolution:CentaurForm")) {
			strengthToApply += AbstractDungeon.player.getPower("evolution:CentaurForm").amount;
		}
		return strengthToApply;
	}

	private static class CentaurAdaptation extends AdaptableEvoCard.AbstractAdaptation {

		CentaurAdaptation(int amount) {
			super(amount);
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
	}
}
