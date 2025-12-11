package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.AntidoteAction;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.patches.AbstractCardEnum;

public class Heal
		extends BaseEvoCard implements StartupCard {
	public static final String ID = "evolutionmod:Heal";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/LymeanSkl.png";
	private static final int COST = 1;
	private static final int CLEANSE_AMT = 2;
	private static final int HEAL_AMT = 3;
	private static final int UPGRADE_HEAL_AMT = 2;

	public Heal() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.RARE, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = HEAL_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new AntidoteAction(CLEANSE_AMT));
	}

	@Override
	public boolean atBattleStartPreDraw() {
		addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber));
		return false;
	}

	@Override
	public AbstractCard makeCopy() {
		return new Heal();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_HEAL_AMT);
			this.initializeDescription();
		}
	}

	public static class AntidoteAction extends AbstractGameAction {

		public AntidoteAction(int amount) {
			this.amount = amount;

			this.actionType = ActionType.CARD_MANIPULATION;
			this.duration = Settings.ACTION_DUR_FAST;
		}

		public void update() {
			if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
				this.isDone = true;
			} else {
				if (AbstractDungeon.player.drawPile.isEmpty()) {
					this.isDone = true;
					return;
				}
				CardGroup statuses = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
				CardGroup drawPile = AbstractDungeon.player.drawPile;
				for (int i = 0; i < Math.min(this.amount, drawPile.size()); ++i) {
					AbstractCard card = drawPile.getRandomCard(AbstractCard.CardType.STATUS, true);
					if (card == null) {
						if (statuses.isEmpty()) {
							this.isDone = true;
							return;
						}
						break;
					}
					statuses.addToTop(card);
					drawPile.removeCard(card);
					// show cards in the center of the screen? Limbo?
				}

				statuses.group.forEach(c ->
						addToTop(new ExhaustSpecificCardAction(c, statuses)));

				this.isDone = true;
			}
		}
	}
}