package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;

public class Toxin
		extends BaseEvoCard {
	public static final String ID = "evolutionmod:Toxin";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
	private static final int COST = 1;
	private static final int POISON_PERCENT_AMT = 10;
	private static final int UPGRADE_POISON_PERCENT_AMT = 5;

	public Toxin() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.ENEMY);
		this.magicNumber = this.baseMagicNumber = POISON_PERCENT_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (!AbstractGene.isPlayerInThisForm(PlantGene.ID)) {
			addToBot(new ChannelAction(new PlantGene()));
		} else if (!AbstractGene.isPlayerInThisForm(LizardGene.ID)) {
			addToBot(new ChannelAction(new LizardGene()));
		} else {
			int poison = m.maxHealth * this.magicNumber / 100;
			addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, poison)));
			this.exhaust = true;
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Toxin();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_POISON_PERCENT_AMT);
		}
	}
}
