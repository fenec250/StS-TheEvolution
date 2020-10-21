package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.Iterator;

public class Treasure extends BaseEvoCard {
	public static final String ID = "evolutionmod:Treasure";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalStone.png";
	private static final int COST = 0;

	public Treasure() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.COMMON, CardTarget.SELF);
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> cardChoices = new ArrayList();
		cardChoices.add(new CrystalStone());
		cardChoices.add(new CrystalShard());
		cardChoices.add(new CrystalDust());
		if (this.upgraded) {
			Iterator var4 = cardChoices.iterator();

			while(var4.hasNext()) {
				AbstractCard c = (AbstractCard)var4.next();
				c.upgrade();
			}
		}
		this.addToBot(new ChooseOneAction(cardChoices));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Treasure();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
		}
	}
}