package evolutionmod.orbs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import evolutionmod.cards.AdaptableEvoCard;

public class BeastGene extends AbstractGene {
	public static final String ID = "evolutionmod:BeastGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/BeastGene.png";

	public BeastGene() {
		super(ID, NAME, "first", IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new BeastGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		int strengthToApply = dexterityToApply() * times;
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, strengthToApply), strengthToApply));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseDexterityPower(p, strengthToApply), strengthToApply));
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = "#yPassive and #yEvoke: " + buildDescription();
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new BeastAdaptation(1);
	}

	private static String buildDescription() {
		return DESCRIPTION[0] + dexterityToApply() + DESCRIPTION[1];
	}

	private static int dexterityToApply() {
		int dexterityToApply = 1;
		if (AbstractDungeon.player.hasPower("evolution:BeastForm")) {
			dexterityToApply += AbstractDungeon.player.getPower("evolution:BeastForm").amount;
		}
		return dexterityToApply;
	}

	private static class BeastAdaptation extends AdaptableEvoCard.AbstractAdaptation {

		BeastAdaptation(int amount) {
			super(amount);
		}

		@Override
		public void apply(AbstractPlayer p, AbstractMonster m) {
			BeastGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Beast";
		}

		@Override
		public String getGeneId() {
			return ID;
		}
	}
}