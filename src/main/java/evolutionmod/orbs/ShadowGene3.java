package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.ShadowGeneAction2;
import evolutionmod.cards.AdaptableEvoCard;

public class ShadowGene3 extends AbstractGene {
	public static final String ID = "evolutionmod:ShadowGene2";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR_STRING = "[#8868A8]";
	public static final Color COLOR = new Color(0x8868A800);
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final TooltipInfo TOOLTIP = new TooltipInfo(COLOR_STRING + NAME + "[]", "Orb: " + getOrbDescription());
	public static final String IMG_PATH = "evolutionmod/images/orbs/ShadowGene.png";
	public static final int CREEPING_SHADOWS = 1;

	public ShadowGene3() {
		super(ID, NAME, getDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1, false);
	}

	@Override
	public void onEvoke() {
//		apply(AbstractDungeon.player, null, 1, true);
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
		return new ShadowGene3();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times, boolean addToTop) {
		int shadows = creepingShadows();
		for (int i = 0; i < times; ++i) {
			if (addToTop) {
				AbstractDungeon.actionManager.addToTop(new ShadowGeneAction2(p, m, damage(), weak()));
			} else {
				AbstractDungeon.actionManager.addToBottom(new ShadowGeneAction2(p, m, damage(), weak()));
			}
		}
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = getOrbDescription();
	}

	public static String getOrbDescription() {
		return DESCRIPTION[2] + getDescription();
	}

	public static String getDescription() {
//		return DESCRIPTION[0] + creepingShadows() + DESCRIPTION[1];
		return DESCRIPTION[0] + damage() + DESCRIPTION[1] + weak() + DESCRIPTION[2];
//		return DESCRIPTION[0] + damage() + DESCRIPTION[1] + weak() + DESCRIPTION[2];
	}

	private static int creepingShadows() {
		return CREEPING_SHADOWS;
	}
	private static int weak() {
		return 1;
	}
	private static int damage() {
		return 3;
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
			ShadowGene3.apply(p, m, this.amount, true);
		}

		@Override
		public String text() {
			return "Ghost";
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

	public static boolean IsPlayerInThisForm() {
		return AbstractDungeon.player.orbs.stream()
				.anyMatch((orb) -> (orb.getClass() == BeastGene.class));
	}
}
