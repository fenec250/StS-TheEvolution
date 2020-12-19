package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.PotencyPower;

import java.util.List;

public class CentaurGene extends AbstractGene {
	public static final String ID = "evolutionmod:CentaurGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR = "[#888888]";
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/CentaurGene.png";
	public static final int VIGOR = 3;

	public CentaurGene() {
		super(ID, NAME, getDescription(), IMG_PATH, COLOR);
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
		int vigorToApply = vigor() * times;
		if (vigorToApply > 0) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VigorPower(p, vigorToApply)));
		}
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	@Override
	public void updateDescription() {
		this.description = getOrbDescription();
	}

	public static String getOrbDescription() {
		return "At the #bstart #bof #byour #bturn and when #yEvoked: NL " + getDescription();
	}

	public static String getDescription() {
		return DESCRIPTION[0] + vigor() + DESCRIPTION[1];
	}

	private static int vigor() {
		int vigor = VIGOR;
		if (CardCrawlGame.isInARun()) {
			if (AbstractDungeon.player.hasPower(PotencyPower.POWER_ID)) {
				vigor += AbstractDungeon.player.getPower(PotencyPower.POWER_ID).amount;
			}
		}
		return vigor;
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
