package evolutionmod.orbs;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.LavafolkGeneAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.EruptionPower;
import evolutionmod.powers.LavafolkFormPower;

public class LavafolkGene extends AbstractGene {
	public static final String ID = "evolutionmod:LavafolkGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/LavafolkGene.png";

	public LavafolkGene() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onEndOfTurn() {
		super.onEndOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new LavafolkGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		for (int i = 0; i < times; ++i) {
			AbstractDungeon.actionManager.addToBottom(new LavafolkGeneAction(p, damagePerGene()));
		}
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	public static AdaptableEvoCard.AbstractAdaptation getAdaptation(int amount, int maximum) {
		return new Adaptation(amount, maximum);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = "#yPassive: " + buildDescription();
	}

	private static String buildDescription() {
		return DESCRIPTION[0] + damagePerGene() + DESCRIPTION[1];
	}

	private static int damagePerGene() {
		int damage = 3;
		if (AbstractDungeon.player.hasPower(LavafolkFormPower.POWER_ID)) {
			damage += AbstractDungeon.player.getPower(LavafolkFormPower.POWER_ID).amount;
		}
		if (AbstractDungeon.player.hasPower(EruptionPower.POWER_ID)) {
			damage *= 2;
		}
		return damage;
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
			LavafolkGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Lavafolk";
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
