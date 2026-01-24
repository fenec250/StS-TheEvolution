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
import evolutionmod.actions.LavafolkGeneAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.cards.Salamander;
import evolutionmod.powers.SalamanderPower;

public class LavafolkGene2 extends AbstractGene {
	public static final String ID = "evolutionmod:LavafolkGene2";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR_STRING = "[#FF9051]";
	public static final Color COLOR = new Color(0xFF905000);
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final TooltipInfo TOOLTIP = new TooltipInfo(COLOR_STRING + NAME + "[]", "Orb: " + getOrbDescription());
	public static final String IMG_PATH = "evolutionmod/images/orbs/LavafolkGene.png";
	public static final int DAMAGE = 2;
	public static final int STRIKE_NB = 2;

	public LavafolkGene2() {
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
		return new LavafolkGene2();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times, boolean addToTop) {
		int damage = damage();
		if (damage > 0) {
			for (int i = 0; i < strikeNb() * times; ++i) {
				if (addToTop) {
					AbstractDungeon.actionManager.addToTop(new LavafolkGeneAction(p, m, damage));
				} else {
					AbstractDungeon.actionManager.addToBottom(new LavafolkGeneAction(p, m, damage));
				}
			}
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
		this.description = getOrbDescription();
	}

	public static String getOrbDescription() {
		return DESCRIPTION[5] + getDescription();
	}

	public static String getDescription() {
		int strikes = strikeNb();
		return DESCRIPTION[0] + damage() + DESCRIPTION[1] + (strikes > 1 ? DESCRIPTION[2] + strikes + DESCRIPTION[3] + DESCRIPTION[4] : DESCRIPTION[4]);
	}

	private static int damage() {
		int damage = DAMAGE
				+ SalamanderPower.getLavafolkExtraDamage()
				+ Salamander.SalamanderPower.getExtraDamage();
		return damage;
	}

	private static int strikeNb() {
		return STRIKE_NB;
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
			LavafolkGene2.apply(p, m, this.amount, true);
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
