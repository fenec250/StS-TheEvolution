package evolutionmod.orbs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.MerfolkFormPower;

public class MerfolkGene extends AbstractGene {
	public static final String ID = "evolutionmod:MerfolkGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public MerfolkGene() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onEndOfTurn() {
		super.onEndOfTurn();
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
		int blockAmount = blockPerGene() * times;
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, blockAmount));
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new MerfolkAdaptation(1);
	}

	@Override
	public String coloredName(boolean plural) {
		return plural
		? "[#5252FF]Merfolk genes[]"
		: "[#5252FF]Merfolk gene[]";
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = "#yPassive: " + buildDescription();
	}

	private static String buildDescription() {
		return DESCRIPTION[0] + blockPerGene() + DESCRIPTION[1];
	}

	private static int blockPerGene() {
		int block = 3;
		if (AbstractDungeon.player.hasPower(MerfolkFormPower.POWER_ID)) {
			block += AbstractDungeon.player.getPower(MerfolkFormPower.POWER_ID).amount;
		}
		return block;
	}

	private static class MerfolkAdaptation extends AdaptableEvoCard.AbstractAdaptation {

		MerfolkAdaptation(int amount) {
			super(amount);
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
	}
}
