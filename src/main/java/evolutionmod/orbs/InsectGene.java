package evolutionmod.orbs;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.LavafolkGeneAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.cards.Drone;
import evolutionmod.powers.EruptionPower;
import evolutionmod.powers.LavafolkFormPower;

public class InsectGene extends AbstractGene {
	public static final String ID = "evolutionmod:InsectGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public InsectGene() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new InsectGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Drone(), times));
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new InsectAdaptation(1);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = "#yPassive: " + buildDescription();
	}

	private static String buildDescription() {
		return DESCRIPTION[0];
	}

	private static class InsectAdaptation extends AdaptableEvoCard.AbstractAdaptation {

		InsectAdaptation(int amount) {
			super(amount);
		}

		@Override
		public void apply(AbstractPlayer p, AbstractMonster m) {
			InsectGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Insect";
		}

		@Override
		public String getGeneId() {
			return ID;
		}

		@Override
		public AdaptableEvoCard.AbstractAdaptation makeCopy() {
			return new InsectAdaptation(this.amount);
		}
	}
}
