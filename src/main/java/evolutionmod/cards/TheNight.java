package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.TheNightPower;

public class TheNight extends BaseEvoCard {
    public static final String ID = "evolutionmod:TheNight";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/TheNight.png";
    private static final int COST = 2;
    private static final int NIGHT_AMT = 2;
    private static final int UPGRADE_NIGHT_AMT = 1;

    public TheNight() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = NIGHT_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        if (!upgraded) {
            this.addToBot(new ApplyPowerAction(p, p, new TheNightPower(p, this.magicNumber)));
            addToBot(new ShadowGene().getChannelAction());
//        } else {
//            this.addToBot(new ApplyPowerAction(p, p, new TheNightPlusPower(p, this.magicNumber)));
//        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheNight();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_NIGHT_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            this.initializeDescription();
        }
    }

//    @Override
//    public List<TooltipInfo> getCustomTooltips() {
//        if (customTooltips == null) {
//            super.getCustomTooltips();
//            customTooltips.add(new TooltipInfo("Quicken Shadows",
//                    "Reduces the amount2 of Shadows by #b1 and its damage by #b2. NL If you have no Shadows apply 1 Weak to ALL enemies instead."));
//        }
//        return  customTooltips;
//    }
}