package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.CrystalShapingAction;
import evolutionmod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CrystalShaping extends BaseEvoCard {
	public static final String ID = "evolutionmod:CrystalShaping";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalStone.png";
	private static final int COST = 0;
	private static final int EXHUME_AMT = 1;

	public CrystalShaping() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = EXHUME_AMT;
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
//		ArrayList<AbstractCard> cardChoices = new ArrayList<>(p.exhaustPile.group.stream()
//				.filter(c -> c.cardID != null
//		&& (c.cardID.equals(CrystalStone.ID)
//		|| c.cardID.equals(CrystalShard.ID)
//		|| c.cardID.equals(CrystalDust.ID)))
//				.collect(Collectors.toList()));
//		if (cardChoices.size() > 0) {
//			this.addToBot(new ChooseOneAction(cardChoices));
//		}
		this.addToBot(new CrystalShapingAction(this.magicNumber));
	}

	@Override
	public AbstractCard makeCopy() {
		return new CrystalShaping();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.exhaust = false;
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}