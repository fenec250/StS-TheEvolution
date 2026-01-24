package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import evolutionmod.cards.AdaptableEvoCard;

public class CentaurGene2 extends AbstractGene {
	public static final String ID = "evolutionmod:CentaurGene2";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR_STRING = "[#888889]";
	public static final Color COLOR = new Color(0x88888800);
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final TooltipInfo TOOLTIP = new TooltipInfo(COLOR_STRING + NAME + "[]", "Orb: " + getOrbDescription());
	public static final String IMG_PATH = "evolutionmod/images/orbs/CentaurGene.png";
	public static final int VIGOR = 3;

	public CentaurGene2() {
		super(ID, NAME, getDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1, false);
	}

	@Override
	public void onEvoke() {
//		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractGameAction getChannelAction() {
		AbstractGene gene = this;
		return new AbstractGameAction() {
			@Override
			public void update() {
				apply(AbstractDungeon.player, null, 1, true);
				addToTop(new ChannelAction(gene));
				this.isDone = true;
			}
		};
	}

	@Override
	public AbstractOrb makeCopy() {
		return new CentaurGene2();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times, boolean addToTop) {
		int vigorToApply = vigor() * times;
		if (vigorToApply > 0) {
			if (addToTop) {
				AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new VigorPower(p, vigorToApply)));
			} else {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VigorPower(p, vigorToApply)));
			}
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
		return DESCRIPTION[2] + getDescription();
	}

	public static String getDescription() {
		return DESCRIPTION[0] + vigor() + DESCRIPTION[1];
	}

	private static int vigor() {
		return VIGOR;
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
			CentaurGene2.apply(p, m, this.amount, true);
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
