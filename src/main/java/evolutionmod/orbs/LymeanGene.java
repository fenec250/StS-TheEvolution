package evolutionmod.orbs;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.LymeanFormPower;

public class LymeanGene extends AbstractGene {
	public static final String ID = "evolutionmod:LymeanGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public LymeanGene() {
		super(ID, NAME, buildDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new LymeanGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		AbstractDungeon.actionManager.addToTop(
				new HealAction(p, p, healPerGene() * times));
		AbstractDungeon.actionManager.addToTop(new GainBlockAction(p, p, blockPerGene() * times));
//		CardGroup cards = new CardGroup(p.hand, CardGroup.CardGroupType.UNSPECIFIED);
//		if (p.hasPower("LymeanForm")) {
//			p.drawPile.group.stream().limit(amount).forEach(cards::addToTop);
//		}
//		cards.group = new ArrayList<>(cards.group.stream()
//				.filter(card -> card.type == AbstractCard.CardType.STATUS)
//				.collect(Collectors.toList()));
//		for (int i = 0; i < times && !cards.isEmpty(); ++i) {
//			AbstractCard card = cards.getRandomCard(AbstractDungeon.cardRng);
//			cards.removeCard(card);
//			AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, cards, true));
//		}
//		AbstractDungeon.cardRng.random(cards.size() - 1);
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new LymeanAdaptation(1);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = "#yPassive: " + buildDescription();
	}

	private static String buildDescription() {
//		return DESCRIPTION[0] + healPerGene() + DESCRIPTION[1] + exhaustPerGene() + DESCRIPTION[2];
		return DESCRIPTION[0] + healPerGene() + DESCRIPTION[1] + blockPerGene() + DESCRIPTION[2];
	}

	private static int healPerGene() {
		return 1;
	}

	private static int blockPerGene() {
		int block = 1;
		if (AbstractDungeon.player.hasPower(LymeanFormPower.POWER_ID)) {
			block += AbstractDungeon.player.getPower(LymeanFormPower.POWER_ID).amount;
		}
		return block;
	}
//
//	private static int exhaustPerGene() {
//		int exhaust = 1;
//		return exhaust;
//	}

	private static class LymeanAdaptation extends AdaptableEvoCard.AbstractAdaptation {

		LymeanAdaptation(int amount) {
			super(amount);
		}

		@Override
		public void apply(AbstractPlayer p, AbstractMonster m) {
			LymeanGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Lymean";
		}

		@Override
		public String getGeneId() {
			return ID;
		}

		@Override
		public AdaptableEvoCard.AbstractAdaptation makeCopy() {
			return new LymeanAdaptation(this.amount);
		}
	}
}
