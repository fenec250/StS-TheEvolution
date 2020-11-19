package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.LustPower;

public class Charm
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Charm";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SuccubusSkl.png";
    private static final int COST = 1;
    private static final int REDUCTION_AMT = 3;
    private static final int UPGRADE_REDUCTION_AMT = 2;
    private static final int FORM_POTENCY = 1;

    public Charm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = REDUCTION_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = this.magicNumber;
        boolean inForm = BaseEvoCard.formEffect(SuccubusGene.ID);
        if (inForm) {
            amount  += 2 + (upgraded ? 1 : 0);
        }
//            addToBot(new ApplyPowerAction(m, p, new LustPower(m, 2 + (upgraded ? 1 : 0))));
//            addToBot(new ApplyPowerAction(p, p, new PotencyPower(p, FORM_POTENCY)));
//            addToBot(new ApplyPowerAction(p, p, new RemovePotencyPower(p, FORM_POTENCY)));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new LustPower(m, amount), amount));
//        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new StrengthPower(m, -amount), -amount));
//        if (m != null && !m.hasPower("Artifact")) {
//            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new GainStrengthPower(m, amount), amount));
//        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Charm();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_REDUCTION_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}