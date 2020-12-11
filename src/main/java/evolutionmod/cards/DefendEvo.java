package evolutionmod.cards;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.FateAction;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.EruptionPower;

public class DefendEvo
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:DefendEvo";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CrystalDust.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 5;
    private static final int UPGRADE_BLOCK_AMT = 3;

    public DefendEvo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.BASIC, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.tags.add(BaseModCardTags.BASIC_DEFEND);
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
//        if (p.hasPower(AdaptationPower.POWER_ID)) {
//            addToTop(new ApplyPowerAction(p, p, new AdaptationPower(p, -1), -1));
//            p.orbs.stream()
//                    .filter(o -> this.canAdaptWith(o) > 0)
//                    .findAny()
//                    .ifPresent(o -> this.tryAdaptingWith(o, true));
//        }
//        this.useAdaptations(p, m);
//        addToBot(new FateAction(2));
//        addToBot(new FateAction(2));
//        addToBot(new AbstractGameAction() {
//            @Override
//            public void update() {
//                addToBot(new FateAction(2));
//                this.isDone = true;
//            }
//        });
//        addToBot(new FateAction(2));
//        addToBot(new ChannelAction(new LymeanGene()));
//        addToBot(new ChannelAction(new LymeanGene()));
//        addToBot(new ChannelAction(new LymeanGene()));
//        addToBot(new ChannelAction(new LymeanGene()));
//        addToBot(new ApplyPowerAction(p, p, new EruptionPower(p, 1)));
    }

    @Override
//    public int canAdaptWith(AbstractAdaptation adaptation) {
//        return adaptation.amount;
//    }
    public int canAdaptWith(AbstractAdaptation adaptation) {
        return 0;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
        }
    }
}