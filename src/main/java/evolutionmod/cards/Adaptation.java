package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.AdaptationAction;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.AdaptationPower;

public class Adaptation
		extends CustomCard {
	public static final String ID = "evolutionmod:Adaptation";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
	private static final int COST = 2;
	private static final int BLOCK_AMT = 10;
	private static final int UPGRADE_BLOCK_AMT = 3;
	private static final int ADAPT_AMT = 2;
	private static final int UPGRADE_ADAPT_AMT = 1;

	public Adaptation() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.BASIC, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = ADAPT_AMT;
		this.block = this.baseBlock = BLOCK_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new GainBlockAction(p, this.block));
		addToBot(new ApplyPowerAction(p, p, new AdaptationPower(p, this.magicNumber), this.magicNumber));
//		AbstractDungeon.actionManager.addToBottom(new AdaptationAction(p, this.magicNumber, this.upgraded));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Adaptation();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_ADAPT_AMT);
			this.upgradeBlock(UPGRADE_BLOCK_AMT);
		}
	}
}