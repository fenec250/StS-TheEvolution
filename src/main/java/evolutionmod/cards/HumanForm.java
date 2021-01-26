package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.HumanFormPower;

import java.util.stream.Collectors;

public class HumanForm extends BaseEvoCard {
    public static final String ID = "evolutionmod:HumanForm";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/HumanForm.png";
    private static final int COST = 3;
//    private static final int UPGRADED_COST = 2;
    private static final int OMNI_AMT = 2;
//    private static final int UPGRADE_OMNI_AMT = 1;

    public HumanForm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = OMNI_AMT;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                p.orbs.stream()
                        .filter(o -> o instanceof AbstractGene)
                        .collect(Collectors.toList())
                        .forEach(o -> addToTop(new EvokeSpecificOrbAction(o)));
                this.isDone = true;
            }
        });
        this.addToBot(new ApplyPowerAction(p, p, new HumanFormPower(p, this.magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new HumanForm();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isEthereal = false;
//            this.upgradeBaseCost(UPGRADED_COST);
//            this.upgradeMagicNumber(UPGRADE_OMNI_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}